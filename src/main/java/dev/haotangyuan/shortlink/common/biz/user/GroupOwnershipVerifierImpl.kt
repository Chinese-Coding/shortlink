package dev.haotangyuan.shortlink.common.biz.user

import cn.hutool.core.util.StrUtil
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import dev.haotangyuan.shortlink.common.constant.RedisKeyConstant.Companion.USER_GIDS_KEY
import dev.haotangyuan.shortlink.common.convention.exception.ClientException
import dev.haotangyuan.shortlink.dao.entity.GroupDO
import dev.haotangyuan.shortlink.dao.mapper.GroupMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.types.Expiration
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class GroupOwnershipVerifierImpl(
    private val stringRedisTemplate: StringRedisTemplate,
    private val groupMapper: GroupMapper,
) : GroupOwnershipVerifier {

    @field:Value("\${short-link.session-ttl-minutes:30}")
    private var sessionTtlMinutes: Int = 0

    override fun assertOwnedByCurrentUser(gid: String) {
        if (StrUtil.isBlank(gid)) {
            throw ClientException("分组标识为空")
        }
        // todo: 这个是不是能设置一个拦截器啊? 业务代码就不要管登录还是未登录的问题了吧
        val username = UserContext.getUsername() ?: throw ClientException("用户未登录")
        val setKey = USER_GIDS_KEY.format(username)
        val hit = try {
            stringRedisTemplate.opsForSet().isMember(setKey, gid) == true
        } catch (ex: Exception) {
            log.error("Redis SISMEMBER user-gids error, username={}, gid={}", username, gid, ex)
            false
        }
        if (hit) {
            return
        }

        // 缓存未命中时查库，校验通过后回填索引并设置 TTL。
        val query = KtQueryWrapper(GroupDO::class.java)
            .eq(GroupDO::gid, gid)
            .eq(GroupDO::username, username)
            .eq(GroupDO::delFlag, 0)
        val group = groupMapper.selectOne(query)
        if (StrUtil.isBlank(group?.username)) {
            throw ClientException("用户信息与分组标识不匹配")
        }

        try {
            stringRedisTemplate.opsForSet().add(setKey, gid)
            stringRedisTemplate.expire(setKey, Expiration.from(sessionTtlMinutes.toLong(), TimeUnit.MINUTES))
        } catch (ex: Exception) {
            log.error("Redis SADD/EXPIRE user-gids error, username={}, gid={}", username, gid, ex)
        }
    }

    override fun assertAllOwnedByCurrentUser(gids: List<String>) {
        gids.forEach(::assertOwnedByCurrentUser)
    }

    private companion object {
        val log: Logger = LoggerFactory.getLogger(GroupOwnershipVerifierImpl::class.java)
    }
}
