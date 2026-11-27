package dev.haotangyuan.shortlink.initialize

import dev.haotangyuan.shortlink.common.constant.RedisKeyConstant.Companion.SHORT_LINK_STATS_STREAM_GROUP_KEY
import dev.haotangyuan.shortlink.common.constant.RedisKeyConstant.Companion.SHORT_LINK_STATS_STREAM_TOPIC_KEY
import org.springframework.beans.factory.InitializingBean
import org.springframework.data.redis.connection.stream.ReadOffset
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component

/**
 * 初始化短链接监控消息队列消费者组
 *
 * @author haotangyuan
 */
@Component
open class LinkStatsStreamInitializeTask(private val stringRedisTemplate: StringRedisTemplate) : InitializingBean {
    @Throws(Exception::class)
    override fun afterPropertiesSet() {
        // 从头消费，避免重建组时丢失消息。等价于 XGROUP CREATE key group 0 MKSTREAM。
        try {
            stringRedisTemplate.opsForStream<String, String>().createGroup(
                SHORT_LINK_STATS_STREAM_TOPIC_KEY,
                ReadOffset.from("0-0"),
                SHORT_LINK_STATS_STREAM_GROUP_KEY,
            )
        } catch (exception: Exception) {
            if (exception.message?.contains("BUSYGROUP") != true) {
                throw exception
            }
        }
    }
}
