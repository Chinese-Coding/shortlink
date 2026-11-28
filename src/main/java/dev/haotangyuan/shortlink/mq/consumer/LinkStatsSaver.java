package dev.haotangyuan.shortlink.mq.consumer;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import dev.haotangyuan.shortlink.dao.entity.*;
import dev.haotangyuan.shortlink.dao.mapper.*;
import dev.haotangyuan.shortlink.dto.biz.LinkStatsRecordDTO;
import dev.haotangyuan.shortlink.toolkit.ipgeo.IpGeoClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dev.haotangyuan.shortlink.common.constant.RedisKeyConstant.LOCK_GID_UPDATE_KEY;
import static dev.haotangyuan.shortlink.common.constant.RedisKeyConstant.STATS_HLL_DELTA_KEY;
import static dev.haotangyuan.shortlink.common.constant.RedisKeyConstant.STATS_UIP_ACTIVE_KEY;
import static dev.haotangyuan.shortlink.common.constant.RedisKeyConstant.STATS_UIP_HLL_KEY;
import static dev.haotangyuan.shortlink.common.constant.RedisKeyConstant.STATS_UV_ACTIVE_KEY;
import static dev.haotangyuan.shortlink.common.constant.RedisKeyConstant.STATS_UV_HLL_KEY;

/**
 * 短链接统计持久化（同步串行，DB 事务内）
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LinkStatsSaver {

    private final LinkMapper linkMapper;
    private final LinkGotoMapper linkGotoMapper;
    private final RedissonClient redissonClient;
    private final IpGeoClient ipGeoClient;
    private final LinkAccessStatsMapper linkAccessStatsMapper;
    private final LinkLocaleStatsMapper linkLocaleStatsMapper;
    private final LinkOsStatsMapper linkOsStatsMapper;
    private final LinkBrowserStatsMapper linkBrowserStatsMapper;
    private final LinkAccessLogsMapper linkAccessLogsMapper;
    private final LinkDeviceStatsMapper linkDeviceStatsMapper;
    private final LinkNetworkStatsMapper linkNetworkStatsMapper;
    private final LinkFirstVisitMapper linkFirstVisitMapper;
    private final StringRedisTemplate stringRedisTemplate;

    private DefaultRedisScript<Long> hllCountAddDeltaScript;

    // 本地 gid 缓存（1万条，10分钟过期），gid 变更是低频操作，缓存不一致通过乐观重试机制修正
    private final Cache<String, String> gidCache = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    private static final String HLL_COUNT_ADD_DELTA_LUA = "lua/hll_count_add_delta.lua";
    private static final int HLL_TTL_SECONDS = 86_400;
    private static final int HLL_DELTA_TTL_SECONDS = 7 * 86_400;
    private static final ZoneId STATS_ZONE = ZoneId.of("Asia/Shanghai");

    @PostConstruct
    public void init() {
        hllCountAddDeltaScript = new DefaultRedisScript<>();
        hllCountAddDeltaScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(HLL_COUNT_ADD_DELTA_LUA)));
        hllCountAddDeltaScript.setResultType(Long.class);
        log.info("LinkStatsSaver initialized");
    }

    // 失效本地 gid 缓存（当 gid 发生变更时由 LinkServiceImpl 调用）
    public void invalidateGidCache(String fullShortUrl) {
        gidCache.invalidate(fullShortUrl);
        log.debug("Invalidated gid cache for: {}", fullShortUrl);
    }

    /**
     * 保存一次短链访问的明细和聚合统计。统计日期、小时和星期按上海时区从访问时间计算，
     * 访问时间缺失时使用当前时间。PV 固定加 1，UV 和 UIP 分别用访客标识和访问 IP
     * 在 Redis HyperLogLog 中计算近似去重增量。地区和运营商由访问 IP 查询得到。
     *
     * <ul>
     *     <li>首访记录保存短链和访客标识，首次插入时将本次访问标记为首访。</li>
     *     <li>访问明细保存短链、访客标识、访问 IP、浏览器、操作系统、设备、运营商、
     *         地区（国家、省、市）、首访标记和消息 ID。</li>
     *     <li>按短链和日期分别累加地区（含行政区划码）、操作系统、浏览器、设备和运营商的访问次数。</li>
     *     <li>按短链、日期、小时和星期累加 PV、UV、UIP，并更新短链主表的累计值。</li>
     * </ul>
     *
     * <p>数据库写入在同一事务内，访问明细的消息 ID 唯一键冲突会使事务回滚。
     * Redis 按消息 ID 暂存 UV 和 UIP 增量供重试复用，数据库提交后清理增量缓存。</p>
     *
     * @param statsRecord 本次访问的短链、访问时间、访客标识、访问 IP、浏览器、操作系统和设备
     * @param messageId   Redis Stream 消息 ID，用于数据库去重和 Redis 增量缓存
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(LinkStatsRecordDTO statsRecord, String messageId) {
        var fullShortUrl = statsRecord.getFullShortUrl();

        // 入口处转成 Instant，accessTime 缺失时回退为当前时刻
        var accessTime = Optional.ofNullable(statsRecord.getAccessTime())
                .map(Date::toInstant)
                .orElseGet(Instant::now);

        var zoned = accessTime.atZone(STATS_ZONE);
        var localDate = zoned.toLocalDate();

        // 只有写库的统计日期需要 Date，在这里转回去
        var statsDate = Date.from(localDate.atStartOfDay(STATS_ZONE).toInstant());

        // 访问日期的 epochDay 对 2 取余，用于区分昨天还是今天
        var v = (int) (localDate.toEpochDay() % 2);

        // 好多地方用了, 提到前面来
        var visitorId = statsRecord.getVisitorId();
        var uip = statsRecord.getUip();

        // 计算 UV delta (UV 的增量)
        var uvKey = String.format(STATS_UV_HLL_KEY, v, fullShortUrl);
        var uvActiveKey = String.format(STATS_UV_ACTIVE_KEY, v);
        var uvDeltaKey = String.format(STATS_HLL_DELTA_KEY, messageId, "uv");
        var uvDelta = stringRedisTemplate.execute(hllCountAddDeltaScript,
                Arrays.asList(uvKey, uvActiveKey, uvDeltaKey), // KEYS[1..<3]
                visitorId,
                fullShortUrl,
                String.valueOf(HLL_TTL_SECONDS),
                String.valueOf(HLL_DELTA_TTL_SECONDS)
        );

        // 计算 UIP delta (UIP 的增量)
        var uipKey = String.format(STATS_UIP_HLL_KEY, v, fullShortUrl);
        var uipActiveKey = String.format(STATS_UIP_ACTIVE_KEY, v);
        var uipDeltaKey = String.format(STATS_HLL_DELTA_KEY, messageId, "uip");
        var uipDelta = stringRedisTemplate.execute(hllCountAddDeltaScript,
                Arrays.asList(uipKey, uipActiveKey, uipDeltaKey),
                uip,
                fullShortUrl,
                String.valueOf(HLL_TTL_SECONDS),
                String.valueOf(HLL_DELTA_TTL_SECONDS)
        );

        // 查询 IP 地理位置
        var geoInfo = ipGeoClient.query(uip);

        // 1. 首访判定（判重表）
        boolean isFirstVisit = false;
        if (StrUtil.isNotBlank(visitorId)) {
            var affected = linkFirstVisitMapper.insertIgnore(LinkFirstVisitDO.builder()
                    .fullShortUrl(fullShortUrl)
                    .user(visitorId)
                    .build()
            );
            isFirstVisit = (affected > 0);
        }

        // 2. 访问日志（messageId 唯一键兜底重复消费）
        String locale = null;
        if (geoInfo != null) {
            locale = Stream.of(geoInfo.getCountry(), geoInfo.getProvince(), geoInfo.getCity())
                    .filter(StrUtil::isNotBlank)
                    .collect(Collectors.joining("-"));
            if (StrUtil.isBlank(locale)) {
                locale = null;
            }
        }
        linkAccessLogsMapper.insert(LinkAccessLogsDO.builder()
                .fullShortUrl(fullShortUrl)
                .user(visitorId)
                .ip(uip)
                .browser(statsRecord.getBrowser())
                .os(statsRecord.getOs())
                .network(geoInfo != null ? geoInfo.getIsp() : null)
                .device(statsRecord.getDevice())
                .locale(locale)
                .firstFlag(isFirstVisit)
                .messageId(messageId)
                .build());

        var uvDeltaInt = uvDelta != null ? uvDelta.intValue() : 0;
        var uipDeltaInt = uipDelta != null ? uipDelta.intValue() : 0;

        // 3. 地区统计
        linkLocaleStatsMapper.shortLinkLocaleStats(LinkLocaleStatsDO.builder()
                .fullShortUrl(fullShortUrl)
                .date(statsDate)
                .cnt(1)
                .province(geoInfo.getProvince())
                .city(geoInfo.getCity())
                .adcode(geoInfo.getAdcode())
                .country(geoInfo.getCountry())
                .build());

        // 4. 操作系统统计
        linkOsStatsMapper.shortLinkOsStats(LinkOsStatsDO.builder()
                .os(statsRecord.getOs())
                .cnt(1)
                .fullShortUrl(fullShortUrl)
                .date(statsDate)
                .build());

        // 5. 浏览器统计
        linkBrowserStatsMapper.shortLinkBrowserStats(LinkBrowserStatsDO.builder()
                .browser(statsRecord.getBrowser())
                .cnt(1)
                .fullShortUrl(fullShortUrl)
                .date(statsDate)
                .build());

        // 6. 设备统计
        linkDeviceStatsMapper.shortLinkDeviceStats(LinkDeviceStatsDO.builder()
                .device(statsRecord.getDevice())
                .cnt(1)
                .fullShortUrl(fullShortUrl)
                .date(statsDate)
                .build());

        // 7. 网络统计
        linkNetworkStatsMapper.shortLinkNetworkStats(LinkNetworkStatsDO.builder()
                .network(geoInfo != null ? geoInfo.getIsp() : null)
                .cnt(1)
                .fullShortUrl(fullShortUrl)
                .date(statsDate)
                .build());

        // 8. 访问统计（PV/UV/UIP）
        linkAccessStatsMapper.shortLinkAccessStats(LinkAccessStatsDO.builder()
                .pv(1)
                .uv(uvDeltaInt)
                .uip(uipDeltaInt)
                .hour(zoned.getHour())
                .weekday(zoned.getDayOfWeek().getValue())
                .fullShortUrl(fullShortUrl)
                .date(statsDate)
                .build());

        // 9. 更新 link 表统计
        updateLinkAgg(fullShortUrl, uvDeltaInt, uipDeltaInt);
        cleanupDeltaKeysAfterCommit(Arrays.asList(uvDeltaKey, uipDeltaKey));
    }

    private void cleanupDeltaKeysAfterCommit(java.util.List<String> deltaKeys) {
        Runnable cleanup = () -> {
            try {
                stringRedisTemplate.delete(deltaKeys);
            } catch (Exception ex) {
                log.warn("Failed to clean HLL delta keys after commit: {}", ex.getMessage());
            }
        };
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    cleanup.run();
                }
            });
        } else {
            cleanup.run();
        }
    }

    // 乐观更新 + 回源重试
    private void updateLinkAgg(String fullShortUrl, int uvDelta, int uipDelta) {
        String cachedGid = gidCache.getIfPresent(fullShortUrl);
        if (cachedGid != null) {
            int affected = linkMapper.incrementStats(cachedGid, fullShortUrl, 1, uvDelta, uipDelta);
            if (affected > 0) {
                return;
            }
            gidCache.invalidate(fullShortUrl);
        }
        incrementStatsWithReadLock(fullShortUrl, uvDelta, uipDelta);
    }

    // 读锁保护：查询 gid + incrementStats
    private void incrementStatsWithReadLock(String fullShortUrl, int uvDelta, int uipDelta) {
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(String.format(LOCK_GID_UPDATE_KEY, fullShortUrl));
        RLock rLock = readWriteLock.readLock();
        rLock.lock();
        try {
            LinkGotoDO linkGotoDO = linkGotoMapper.selectOne(
                    Wrappers.lambdaQuery(LinkGotoDO.class).eq(LinkGotoDO::getFullShortUrl, fullShortUrl));
            if (linkGotoDO == null) {
                log.warn("Link not found: {}", fullShortUrl);
                return;
            }
            String gid = linkGotoDO.getGid();
            gidCache.put(fullShortUrl, gid);
            int affected = linkMapper.incrementStats(gid, fullShortUrl, 1, uvDelta, uipDelta);
            if (affected == 0) {
                log.warn("incrementStats affected 0, link deleted or gid changed: {}", fullShortUrl);
            }
        } finally {
            rLock.unlock();
        }
    }
}
