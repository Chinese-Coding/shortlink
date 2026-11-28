package dev.haotangyuan.shortlink.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import dev.haotangyuan.shortlink.common.biz.user.GroupOwnershipVerifier;
import dev.haotangyuan.shortlink.common.biz.user.UserContext;
import dev.haotangyuan.shortlink.common.config.GotoDomainWhiteListConfiguration;
import dev.haotangyuan.shortlink.common.convention.exception.ClientException;
import dev.haotangyuan.shortlink.common.convention.exception.ServiceException;
import dev.haotangyuan.shortlink.common.enums.ValidDateTypeEnum;
import dev.haotangyuan.shortlink.dao.entity.LinkDO;
import dev.haotangyuan.shortlink.dao.entity.LinkGotoDO;
import dev.haotangyuan.shortlink.dao.mapper.LinkAccessStatsMapper;
import dev.haotangyuan.shortlink.dao.mapper.LinkGotoMapper;
import dev.haotangyuan.shortlink.dao.mapper.LinkMapper;
import dev.haotangyuan.shortlink.dto.biz.LinkStatsRecordDTO;
import dev.haotangyuan.shortlink.dto.req.LinkBatchCreateReqDTO;
import dev.haotangyuan.shortlink.dto.req.LinkCreateReqDTO;
import dev.haotangyuan.shortlink.dto.req.LinkPageReqDTO;
import dev.haotangyuan.shortlink.dto.req.LinkUpdateReqDTO;
import dev.haotangyuan.shortlink.vo.*;
import dev.haotangyuan.shortlink.mq.consumer.LinkStatsSaver;
import dev.haotangyuan.shortlink.mq.producer.LinkStatsSaveProducer;
import dev.haotangyuan.shortlink.service.LinkService;
import dev.haotangyuan.shortlink.toolkit.LinkUtil;
import dev.haotangyuan.shortlink.toolkit.ShortCodeUtil;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.github.benmanes.caffeine.cache.Cache;
import org.jspecify.annotations.NonNull;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static dev.haotangyuan.shortlink.common.constant.LinkConstant.VISITOR_COOKIE_MAX_AGE_SECONDS;
import static dev.haotangyuan.shortlink.common.constant.RedisKeyConstant.*;
import static dev.haotangyuan.shortlink.common.constant.UserConstant.PUBLIC_GID;
import static dev.haotangyuan.shortlink.common.constant.UserConstant.PUBLIC_USERNAME;

/**
 * 短链接接口实现层
 *
 * @author: haotangyuan
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LinkServiceImpl extends ServiceImpl<LinkMapper, LinkDO> implements LinkService {

    private final RBloomFilter<String> shortUriCreateCachePenetrationBloomFilter;
    private final LinkGotoMapper linkGotoMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedissonClient redissonClient;
    private final LinkAccessStatsMapper linkAccessStatsMapper;
    private final LinkTodayStatsQuery linkTodayStatsQuery;
    private final GotoDomainWhiteListConfiguration gotoDomainWhiteListConfiguration;
    private final LinkStatsSaveProducer linkStatsSaveProducer;
    private final LinkStatsSaver linkStatsSaver;
    private final GroupOwnershipVerifier groupOwnershipService;
    private final LinkUtil linkUtil;
    // 本地每键互斥锁缓存（避免跳转路径使用分布式锁）
    private final Cache<String, ReentrantLock> redirectLockCache;
    // 短链接跳转目标 URL 本地缓存（减少 Redis 网络往返）
    private final Cache<String, String> redirectCache;
    private final TransactionTemplate transactionTemplate;

    private static final int MAX_BATCH_CREATE_SIZE = 100;

    @Value("${short-link.domain.default}")
    private String createLinkDefaultDomain;

    /**
     * 创建短链接，依次校验分组和请求参数、生成短码并写入详情和路由记录。
     * 两张表在同一事务内写入，事务提交后预热 Redis 跳转缓存。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public @NonNull LinkCreateVO createLink(@NonNull LinkCreateReqDTO linkCreateReqDTO) {
        // 匿名请求在用户上下文中标记为 public，强制归入公共分组。
        var currentUsername = UserContext.getUsername();
        if (java.util.Objects.equals(currentUsername, PUBLIC_USERNAME)) {
            linkCreateReqDTO.setGid(PUBLIC_GID);
        } else {
            // 先从 Redis 检查分组归属，缓存未命中时查库确认分组属于当前用户且未删除。
            groupOwnershipService.assertOwnedByCurrentUser(linkCreateReqDTO.getGid());
        }

        // 校验公开 HTTP(S) 地址、URL 和描述长度，以及启用时的域名白名单。
        verificationWhitelist(linkCreateReqDTO.getOriginUrl());
        validateDescription(linkCreateReqDTO.getDescribe());

        // 创建类型缺省为 0。链接统一使用自定义有效期，默认一天，最长三天。
        // 请求指定的过期时间必须晚于当前时间。
        if (linkCreateReqDTO.getCreatedType() == null) {
            linkCreateReqDTO.setCreatedType(0);
        }
        linkCreateReqDTO.setValidDateType(ValidDateTypeEnum.CUSTOM.getType());
        linkCreateReqDTO.setValidDate(normalizeValidDate(linkCreateReqDTO.getValidDate()));

        // 从 Redis 分配给当前实例的号段中取序号，经仿射变换和 Base62 编码得到定长短码。
        var shortCode = ShortCodeUtil.next();
        var fullShortUrl = StrBuilder.create(createLinkDefaultDomain)
                .append("/")
                .append(shortCode)
                .toString();
        // t_link 按 gid 分片，保存跳转地址、有效期和初始统计值等详情。
        var shortLinkDO = LinkDO.builder()
                .domain(createLinkDefaultDomain)
                .originUrl(linkCreateReqDTO.getOriginUrl())
                .gid(linkCreateReqDTO.getGid())
                .createdType(linkCreateReqDTO.getCreatedType())
                .validDateType(linkCreateReqDTO.getValidDateType())
                .validDate(linkCreateReqDTO.getValidDate())
                .describe(linkCreateReqDTO.getDescribe())
                .shortUri(shortCode)
                .enableStatus(0)
                .totalPv(0)
                .totalUv(0)
                .totalUip(0)
                .delTime(0L)
                .fullShortUrl(fullShortUrl)
                .favicon(linkUtil.getFavicon(linkCreateReqDTO.getOriginUrl()))
                .build();
        // t_link_goto 按完整短链分片。跳转缓存未命中时，先查出 gid，再定位详情分片。
        var linkGotoDO = LinkGotoDO.builder()
                .fullShortUrl(fullShortUrl)
                .gid(linkCreateReqDTO.getGid())
                .build();
        // 任一条记录写入失败都会使创建事务回滚。
        try {
            if (baseMapper.insert(shortLinkDO) < 1 || linkGotoMapper.insert(linkGotoDO) < 1) {
                throw new ServiceException("短链接创建失败，请稍后重试");
            }
        } catch (DuplicateKeyException ex) {
            // 唯一键冲突时补齐布隆过滤器，并结束本次创建。
            if (!shortUriCreateCachePenetrationBloomFilter.contains(fullShortUrl)) {
                shortUriCreateCachePenetrationBloomFilter.add(fullShortUrl);
            }
            throw new ServiceException(String.format("短链接：%s 生成重复", fullShortUrl));
        }
        // 布隆过滤器用于跳转时过滤明显不存在的短链。
        shortUriCreateCachePenetrationBloomFilter.add(fullShortUrl);
        // 事务提交后写入跳转映射并清理空值缓存。预热失败仅记录日志。
        warmRedirectCacheAfterCommit(fullShortUrl, linkCreateReqDTO.getOriginUrl(), linkCreateReqDTO.getValidDate());
        return LinkCreateVO.builder()
                .fullShortUrl("http://" + shortLinkDO.getFullShortUrl())
                .originUrl(linkCreateReqDTO.getOriginUrl())
                .gid(linkCreateReqDTO.getGid())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateLink(LinkUpdateReqDTO linkUpdateReqDTO) {
        linkUpdateReqDTO.setFullShortUrl(normalizeFullShortUrl(linkUpdateReqDTO.getFullShortUrl()));
        linkUpdateReqDTO.setValidDateType(ValidDateTypeEnum.CUSTOM.getType());
        linkUpdateReqDTO.setValidDate(normalizeValidDate(linkUpdateReqDTO.getValidDate()));
        validateDescription(linkUpdateReqDTO.getDescribe());
        // 鉴权：旧、新分组均需属于当前用户
        groupOwnershipService.assertOwnedByCurrentUser(linkUpdateReqDTO.getOriginGid());
        groupOwnershipService.assertOwnedByCurrentUser(linkUpdateReqDTO.getGid());
        verificationWhitelist(linkUpdateReqDTO.getOriginUrl());
        LambdaQueryWrapper<LinkDO> queryWrapper = Wrappers.lambdaQuery(LinkDO.class)
                .eq(LinkDO::getGid, linkUpdateReqDTO.getOriginGid())
                .eq(LinkDO::getFullShortUrl, linkUpdateReqDTO.getFullShortUrl())
                .eq(LinkDO::getDelFlag, 0)
                .eq(LinkDO::getEnableStatus, 0);
        LinkDO hasLinkDO = baseMapper.selectOne(queryWrapper);
        if (hasLinkDO == null) {
            throw new ClientException("短链接记录不存在");
        }
        if (Objects.equals(hasLinkDO.getGid(), linkUpdateReqDTO.getGid())) {
            LambdaUpdateWrapper<LinkDO> updateWrapper = Wrappers.lambdaUpdate(LinkDO.class)
                    .eq(LinkDO::getFullShortUrl, linkUpdateReqDTO.getFullShortUrl())
                    .eq(LinkDO::getGid, linkUpdateReqDTO.getGid())
                    .eq(LinkDO::getDelFlag, 0)
                    .eq(LinkDO::getEnableStatus, 0)
                    .set(Objects.equals(linkUpdateReqDTO.getValidDateType(), ValidDateTypeEnum.PERMANENT.getType()), LinkDO::getValidDate, null);
            LinkDO linkDO = LinkDO.builder()
                    .domain(hasLinkDO.getDomain())
                    .shortUri(hasLinkDO.getShortUri())
                    .favicon(Objects.equals(linkUpdateReqDTO.getOriginUrl(), hasLinkDO.getOriginUrl()) ? hasLinkDO.getFavicon() : linkUtil.getFavicon(linkUpdateReqDTO.getOriginUrl()))
                    .createdType(hasLinkDO.getCreatedType())
                    .gid(linkUpdateReqDTO.getGid())
                    .originUrl(linkUpdateReqDTO.getOriginUrl())
                    .describe(linkUpdateReqDTO.getDescribe())
                    .validDateType(linkUpdateReqDTO.getValidDateType())
                    .validDate(linkUpdateReqDTO.getValidDate())
                    .build();
            if (baseMapper.update(linkDO, updateWrapper) < 1) {
                throw new ClientException("短链接更新失败，请刷新后重试");
            }
        } else {
            RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(String.format(LOCK_GID_UPDATE_KEY, linkUpdateReqDTO.getFullShortUrl()));
            RLock rLock = readWriteLock.writeLock();
            rLock.lock();
            try {
                LambdaUpdateWrapper<LinkDO> linkUpdateWrapper = Wrappers.lambdaUpdate(LinkDO.class)
                        .eq(LinkDO::getFullShortUrl, linkUpdateReqDTO.getFullShortUrl())
                        .eq(LinkDO::getGid, hasLinkDO.getGid())
                        .eq(LinkDO::getDelFlag, 0)
                        .eq(LinkDO::getDelTime, 0L)
                        .eq(LinkDO::getEnableStatus, 0);
                LinkDO delLinkDO = LinkDO.builder()
                        .delTime(System.currentTimeMillis())
                        .build();
                delLinkDO.setDelFlag(1);
                if (baseMapper.update(delLinkDO, linkUpdateWrapper) < 1) {
                    throw new ClientException("短链接更新失败，请刷新后重试");
                }
                LinkDO linkDO = LinkDO.builder()
                        .domain(hasLinkDO.getDomain())
                        .originUrl(linkUpdateReqDTO.getOriginUrl())
                        .gid(linkUpdateReqDTO.getGid())
                        .createdType(hasLinkDO.getCreatedType())
                        .validDateType(linkUpdateReqDTO.getValidDateType())
                        .validDate(linkUpdateReqDTO.getValidDate())
                        .describe(linkUpdateReqDTO.getDescribe())
                        .shortUri(hasLinkDO.getShortUri())
                        .enableStatus(hasLinkDO.getEnableStatus())
                        .totalPv(hasLinkDO.getTotalPv())
                        .totalUv(hasLinkDO.getTotalUv())
                        .totalUip(hasLinkDO.getTotalUip())
                        .fullShortUrl(hasLinkDO.getFullShortUrl())
                        .favicon(Objects.equals(linkUpdateReqDTO.getOriginUrl(), hasLinkDO.getOriginUrl()) ? hasLinkDO.getFavicon() : linkUtil.getFavicon(linkUpdateReqDTO.getOriginUrl()))
                        .delTime(0L)
                        .build();
                if (baseMapper.insert(linkDO) < 1) {
                    throw new ClientException("短链接更新失败，请稍后重试");
                }
                UpdateWrapper<LinkGotoDO> linkGotoUpdateWrapper = Wrappers.update();
                linkGotoUpdateWrapper
                        .eq("full_short_url", linkUpdateReqDTO.getFullShortUrl())
                        .eq("gid", hasLinkDO.getGid())
                        .set("gid", linkUpdateReqDTO.getGid());
                if (linkGotoMapper.update(null, linkGotoUpdateWrapper) < 1) {
                    throw new ClientException("短链接路由记录不存在");
                }

                // 失效 gid 缓存
                linkStatsSaver.invalidateGidCache(linkUpdateReqDTO.getFullShortUrl());
            } finally {
                rLock.unlock();
            }
        }
        if (!Objects.equals(hasLinkDO.getValidDateType(), linkUpdateReqDTO.getValidDateType())
                || !Objects.equals(hasLinkDO.getValidDate(), linkUpdateReqDTO.getValidDate())
                || !Objects.equals(hasLinkDO.getOriginUrl(), linkUpdateReqDTO.getOriginUrl())) {
            // 删除 Redis 缓存
            stringRedisTemplate.delete(String.format(GOTO_SHORT_LINK_KEY, linkUpdateReqDTO.getFullShortUrl()));
            // 删除本地 Caffeine 缓存
            redirectCache.invalidate(linkUpdateReqDTO.getFullShortUrl());
            Date currentDate = new Date();
            if (hasLinkDO.getValidDate() != null && hasLinkDO.getValidDate().before(currentDate)) {
                if (Objects.equals(linkUpdateReqDTO.getValidDateType(), ValidDateTypeEnum.PERMANENT.getType())
                        || (linkUpdateReqDTO.getValidDate() != null && linkUpdateReqDTO.getValidDate().after(currentDate))) {
                    stringRedisTemplate.delete(String.format(GOTO_IS_NULL_SHORT_LINK_KEY, linkUpdateReqDTO.getFullShortUrl()));
                }
            }
        }
    }

    @Override
    public IPage<LinkPageVO> pageLink(LinkPageReqDTO linkPageReqDTO) {
        // 鉴权：校验分组归属
        groupOwnershipService.assertOwnedByCurrentUser(linkPageReqDTO.getGid());
        Page<LinkDO> page = new Page<>(linkPageReqDTO.getCurrent(), linkPageReqDTO.getSize());
        IPage<LinkDO> resultPage = baseMapper.pageLink(page, linkPageReqDTO);

        List<String> fullShortUrls = resultPage.getRecords().stream()
                .map(LinkDO::getFullShortUrl)
                .toList();
        Map<String, LinkTodayStatsQuery.TodayStats> todayStatsMap = linkTodayStatsQuery.findByShortUrls(fullShortUrls);

        return resultPage.convert(each -> {
            LinkPageVO bean = BeanUtil.toBean(each, LinkPageVO.class);
            bean.setDomain("http://" + bean.getDomain());

            LinkTodayStatsQuery.TodayStats todayStats = todayStatsMap.get(each.getFullShortUrl());
            if (todayStats != null) {
                bean.setTodayPv(todayStats.pv());
                bean.setTodayUv(todayStats.uv());
                bean.setTodayUip(todayStats.uip());
            } else {
                bean.setTodayPv(0);
                bean.setTodayUv(0);
                bean.setTodayUip(0);
            }

            return bean;
        });
    }

    @Override
    public @NonNull List<GroupLinkCountQueryVO> listGroupLinkCount(@NonNull List<String> gidList) {
        // 鉴权：校验分组列表归属
        groupOwnershipService.assertAllOwnedByCurrentUser(gidList);
        if (CollUtil.isEmpty(gidList)) {
            return Collections.emptyList();
        }
        List<GroupLinkCountQueryVO> groupStats = baseMapper.listGroupLinkCount(gidList);
        ZoneId shanghaiZone = ZoneId.of("Asia/Shanghai");
        Date today = Date.from(LocalDate.now(shanghaiZone).atStartOfDay(shanghaiZone).toInstant());
        Map<String, Long> todayPvByGroup = linkAccessStatsMapper.listTodayPvByGroups(gidList, today).stream()
                .collect(java.util.stream.Collectors.toMap(
                        GroupLinkCountQueryVO::getGid,
                        each -> Optional.ofNullable(each.getTodayPv()).orElse(0L)
                ));
        groupStats.forEach(each -> each.setTodayPv(todayPvByGroup.getOrDefault(each.getGid(), 0L)));
        return groupStats;
    }

    @Override
    public void restoreUrl(String shortUri, ServletRequest request, ServletResponse response) throws IOException {
        // 拼接出完整的短链接
        var serverName = request.getServerName();
        var serverPort = Optional.of(request.getServerPort())
                .filter(each -> !Objects.equals(each, 80))
                .map(String::valueOf)
                .map(each -> ":" + each)
                .orElse("");
        var fullShortUrl = StrBuilder.create(serverName)
                .append(serverPort)
                .append("/")
                .append(shortUri)
                .toString();

        // 1. 优先查询本地 Caffeine 缓存
        var originalLink = redirectCache.getIfPresent(fullShortUrl);
        if (StrUtil.isNotBlank(originalLink)) {
            doRedirect(originalLink, fullShortUrl, request, response);
            return;
        }
        // 2. 查询 Redis 缓存
        originalLink = stringRedisTemplate.opsForValue().get(String.format(GOTO_SHORT_LINK_KEY, fullShortUrl));
        if (StrUtil.isNotBlank(originalLink)) {
            redirectCache.put(fullShortUrl, originalLink);
            doRedirect(originalLink, fullShortUrl, request, response);
            return;
        }
        boolean contains = shortUriCreateCachePenetrationBloomFilter.contains(fullShortUrl);
        if (!contains) {
            doNotFound(response);
            return;
        }
        String gotoIsNullShortLink = stringRedisTemplate.opsForValue().get(String.format(GOTO_IS_NULL_SHORT_LINK_KEY, fullShortUrl));
        if (StrUtil.isNotBlank(gotoIsNullShortLink)) {
            doNotFound(response);
            return;
        }
        // 本地每键互斥：使用 Caffeine 管理 ReentrantLock，避免跳转路径使用分布式锁导致尾延迟放大
        ReentrantLock lock = redirectLockCache.get(fullShortUrl, k -> new ReentrantLock());
        lock.lock();
        try {
            // 双重检查：先查本地缓存
            originalLink = redirectCache.getIfPresent(fullShortUrl);
            if (StrUtil.isNotBlank(originalLink)) {
                doRedirect(originalLink, fullShortUrl, request, response);
                return;
            }
            // 双重检查：再查 Redis
            originalLink = stringRedisTemplate.opsForValue().get(String.format(GOTO_SHORT_LINK_KEY, fullShortUrl));
            if (StrUtil.isNotBlank(originalLink)) {
                redirectCache.put(fullShortUrl, originalLink);
                doRedirect(originalLink, fullShortUrl, request, response);
                return;
            }
            gotoIsNullShortLink = stringRedisTemplate.opsForValue().get(String.format(GOTO_IS_NULL_SHORT_LINK_KEY, fullShortUrl));
            if (StrUtil.isNotBlank(gotoIsNullShortLink)) {
                doNotFound(response);
                return;
            }
            LambdaQueryWrapper<LinkGotoDO> linkGotoQueryWrapper = Wrappers.lambdaQuery(LinkGotoDO.class)
                    .eq(LinkGotoDO::getFullShortUrl, fullShortUrl);
            LinkGotoDO linkGotoDO = linkGotoMapper.selectOne(linkGotoQueryWrapper);
            if (linkGotoDO == null) {
                stringRedisTemplate.opsForValue().set(String.format(GOTO_IS_NULL_SHORT_LINK_KEY, fullShortUrl), "-", 30, TimeUnit.MINUTES);
                doNotFound(response);
                return;
            }
            LambdaQueryWrapper<LinkDO> queryWrapper = Wrappers.lambdaQuery(LinkDO.class)
                    .eq(LinkDO::getGid, linkGotoDO.getGid())
                    .eq(LinkDO::getFullShortUrl, fullShortUrl)
                    .eq(LinkDO::getDelFlag, 0)
                    .eq(LinkDO::getEnableStatus, 0);
            LinkDO linkDO = baseMapper.selectOne(queryWrapper);
            if (linkDO == null || (linkDO.getValidDate() != null && linkDO.getValidDate().before(new Date()))) {
                stringRedisTemplate.opsForValue().set(String.format(GOTO_IS_NULL_SHORT_LINK_KEY, fullShortUrl), "-", 30, TimeUnit.MINUTES);
                doNotFound(response);
                return;
            }
            stringRedisTemplate.opsForValue().set(
                    String.format(GOTO_SHORT_LINK_KEY, fullShortUrl),
                    linkDO.getOriginUrl(),
                    LinkUtil.getLinkCacheValidTime(linkDO.getValidDate()), TimeUnit.MILLISECONDS
            );
            redirectCache.put(fullShortUrl, linkDO.getOriginUrl());
            doRedirect(linkDO.getOriginUrl(), fullShortUrl, request, response);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public @NonNull LinkBatchCreateVO batchCreateLink(LinkBatchCreateReqDTO linkBatchCreateReqDTO) {
        List<String> originUrls = linkBatchCreateReqDTO.getOriginUrls();
        if (CollUtil.isEmpty(originUrls)) {
            throw new ClientException("批量创建链接不能为空");
        }
        if (originUrls.size() > MAX_BATCH_CREATE_SIZE) {
            throw new ClientException(String.format("单次最多创建 %d 条短链接", MAX_BATCH_CREATE_SIZE));
        }
        if (originUrls.stream().anyMatch(StrUtil::isBlank)) {
            throw new ClientException("原始链接不能为空");
        }
        List<String> describes = linkBatchCreateReqDTO.getDescribes();
        List<LinkBaseInfoVO> result = new ArrayList<>();
        for (int i = 0; i < originUrls.size(); i++) {
            LinkCreateReqDTO shortLinkCreateReqDTO = BeanUtil.toBean(linkBatchCreateReqDTO, LinkCreateReqDTO.class);
            shortLinkCreateReqDTO.setOriginUrl(originUrls.get(i));
            String desc = (describes != null && i < describes.size()) ? describes.get(i) : null;
            shortLinkCreateReqDTO.setDescribe(desc);
            try {
                LinkCreateVO shortLink = transactionTemplate.execute(status -> createLink(shortLinkCreateReqDTO));
                if (shortLink == null) {
                    throw new ServiceException("短链接创建失败，请稍后重试");
                }
                LinkBaseInfoVO linkBaseInfoRespDTO = LinkBaseInfoVO.builder()
                        .fullShortUrl(shortLink.getFullShortUrl())
                        .originUrl(shortLink.getOriginUrl())
                        .describe(desc)
                        .build();
                result.add(linkBaseInfoRespDTO);
            } catch (Exception ex) {
                log.warn("批量创建第 {} 项失败，异常类型={}", i + 1, ex.getClass().getSimpleName());
            }
        }
        return LinkBatchCreateVO.builder()
                .total(result.size())
                .baseLinkInfos(result)
                .build();
    }

    private Date normalizeValidDate(Date validDate) {
        var now = ZonedDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        if (validDate == null) {
            return Date.from(now.plusDays(1).toInstant());
        }
        if (validDate.getTime() <= now.toInstant().toEpochMilli()) {
            throw new ClientException("有效期必须晚于当前时间");
        }
        var maxValidDate = Date.from(now.plusDays(3).toInstant());
        return validDate.after(maxValidDate) ? maxValidDate : validDate;
    }

    private void validateDescription(String description) {
        if (description != null && description.length() > 1024) {
            throw new ClientException("短链接描述不能超过 1024 个字符");
        }
    }

    private void warmRedirectCacheAfterCommit(String fullShortUrl, String originUrl, Date validDate) {
        Runnable warmCache = () -> {
            try {
                stringRedisTemplate.opsForValue().set(
                        String.format(GOTO_SHORT_LINK_KEY, fullShortUrl),
                        originUrl,
                        Expiration.milliseconds(LinkUtil.getLinkCacheValidTime(validDate))
                );
                stringRedisTemplate.delete(String.format(GOTO_IS_NULL_SHORT_LINK_KEY, fullShortUrl));
            } catch (Exception ex) {
                log.warn("Warm redirect cache after create failed, fullShortUrl={}", fullShortUrl, ex);
            }
        };
        // 如果同步事务已经启动, 就等事务成功后再调用
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    warmCache.run();
                }
            });
        } else {
            warmCache.run();
        }
    }

    /**
     * 确定本次访问的浏览器访客 ID，并组装供 doRedirect 入队的统计记录。
     */
    private LinkStatsRecordDTO buildLinkStatsRecordAndSetUser(String fullShortUrl, ServletRequest request, ServletResponse response) {
        var cookies = ((HttpServletRequest) request).getCookies();
        // Cookie 查找和生成都在当前请求线程执行，使用可变容器让 Lambda 保存访客 ID。
        var visitorId = new AtomicReference<String>();
        // 缺少 visitorId Cookie 时生成 UUID，通过响应发给浏览器，最长保留 3 个月。
        Runnable addResponseCookieTask = () -> {
            visitorId.set(UUID.fastUUID().toString());
            var visitorCookie = new Cookie("visitorId", visitorId.get());
            visitorCookie.setMaxAge(VISITOR_COOKIE_MAX_AGE_SECONDS);
            // 路径使用当前短链的路径，例如 /Ab123x，实现按短链识别访客。
            visitorCookie.setPath(StrUtil.sub(fullShortUrl, fullShortUrl.indexOf("/"), fullShortUrl.length()));
            ((HttpServletResponse) response).addCookie(visitorCookie);
        };
        // 复用请求中的第一个 visitorId Cookie，保留原有有效期。
        if (ArrayUtil.isNotEmpty(cookies)) {
            Arrays.stream(cookies)
                    .filter(each -> Objects.equals(each.getName(), "visitorId"))
                    .findFirst()
                    .map(Cookie::getValue)
                    .ifPresentOrElse(visitorId::set, addResponseCookieTask);
        } else {
            addResponseCookieTask.run();
        }
        // 访客 ID 用于 UV 去重，uip 保存本次请求 IP，同时采集访问环境和时间。
        var uip = LinkUtil.getActualIp(((HttpServletRequest) request));
        var os = LinkUtil.getOs(((HttpServletRequest) request));
        var browser = LinkUtil.getBrowser(((HttpServletRequest) request));
        var device = LinkUtil.getDevice(((HttpServletRequest) request));
        return LinkStatsRecordDTO.builder()
                .fullShortUrl(fullShortUrl)
                .visitorId(visitorId.get())
                .uip(uip)
                .os(os)
                .browser(browser)
                .device(device)
                .accessTime(new Date())
                .build();
    }

    @Override
    public void linkStats(@NonNull LinkStatsRecordDTO linkStatsRecordDTO) {
        var producerMap = new HashMap<String, String>();
        // todo: 直接这么写, 而不商定一个变量去存储, 这样好吗?
        producerMap.put("statsRecord", JSON.toJSONString(linkStatsRecordDTO));
        linkStatsSaveProducer.send(producerMap);
    }

    private void verificationWhitelist(String originUrl) {
        if (!LinkUtil.isPublicHttpUrl(originUrl) || originUrl.length() > 1024) {
            throw new ClientException("跳转链接填写错误");
        }

        var enable = gotoDomainWhiteListConfiguration.getEnable();
        if (enable == null || !enable) {
            return;
        }

        var domain = LinkUtil.extractDomain(originUrl);
        if (StrUtil.isBlank(domain)) {
            throw new ClientException("跳转链接填写错误");
        }

        var details = gotoDomainWhiteListConfiguration.getDetails();
        if (!details.contains(domain)) {
            throw new ClientException("演示环境为避免恶意攻击，请生成以下网站跳转链接：" + gotoDomainWhiteListConfiguration.getNames());
        }
    }

    private String normalizeFullShortUrl(String fullShortUrl) {
        if (StrUtil.isBlank(fullShortUrl)) {
            throw new ClientException("短链接不能为空");
        }
        if (StrUtil.startWithIgnoreCase(fullShortUrl, "http://")) {
            return fullShortUrl.substring(7);
        }
        if (StrUtil.startWithIgnoreCase(fullShortUrl, "https://")) {
            return fullShortUrl.substring(8);
        }
        return fullShortUrl;
    }

    private void doRedirect(String targetUrl, String fullShortUrl, ServletRequest request, ServletResponse response) throws IOException {
        try {
            linkStats(buildLinkStatsRecordAndSetUser(fullShortUrl, request, response));
        } catch (Exception ex) {
            log.warn("Enqueue redirect statistics failed, fullShortUrl={}, reason={}", fullShortUrl, ex.getMessage());
        }
        ((HttpServletResponse) response).sendRedirect(targetUrl);
    }

    private void doNotFound(ServletResponse response) throws IOException {
        ((HttpServletResponse) response).sendRedirect("/page/notfound");
    }
}
