package dev.haotangyuan.shortlink.mq.task

import dev.haotangyuan.shortlink.common.constant.RedisKeyConstant.Companion.SHORT_LINK_STATS_STREAM_GROUP_KEY
import dev.haotangyuan.shortlink.common.constant.RedisKeyConstant.Companion.SHORT_LINK_STATS_STREAM_TOPIC_KEY
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.script.DefaultRedisScript
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.scripting.support.ResourceScriptSource
import org.springframework.stereotype.Component

/**
 * Stream 消息清理任务：保留 Pending 消息和最近已消费的缓冲。
 *
 * @author haotangyuan
 */
@Component
class StreamCleanupTask(private val stringRedisTemplate: StringRedisTemplate) {
    private var trimScript: DefaultRedisScript<Long>? = null

    @PostConstruct
    fun initTrimScript() {
        trimScript = DefaultRedisScript<Long>().apply {
            resultType = Long::class.javaObjectType
            setScriptSource(ResourceScriptSource(ClassPathResource(STREAM_TRIM_MINID_LUA_PATH)))
        }
    }

    @Scheduled(fixedRate = 300_000)
    fun cleanupConsumedMessages() {
        try {
            val trimmed = stringRedisTemplate.execute(
                checkNotNull(trimScript),
                listOf(SHORT_LINK_STATS_STREAM_TOPIC_KEY),
                SHORT_LINK_STATS_STREAM_GROUP_KEY,
                CONSUMED_BUFFER_SIZE.toString(),
            )
            if (trimmed != null && trimmed > 0) {
                log.info("Stream 清理成功: 删除 {} 条消息", trimmed)
            } else {
                log.debug("Stream 清理执行: 本轮无可删消息")
            }
        } catch (exception: Exception) {
            log.error("Stream 清理失败", exception)
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(StreamCleanupTask::class.java)
        private const val CONSUMED_BUFFER_SIZE = 3000L
        private const val STREAM_TRIM_MINID_LUA_PATH = "lua/stream_trim_minid.lua"
    }
}
