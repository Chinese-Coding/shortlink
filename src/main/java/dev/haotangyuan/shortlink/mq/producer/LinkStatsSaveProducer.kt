package dev.haotangyuan.shortlink.mq.producer

import dev.haotangyuan.shortlink.common.constant.RedisKeyConstant.Companion.SHORT_LINK_STATS_STREAM_TOPIC_KEY
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component

/**
 * 短链接监控状态保存消息队列生产者
 *
 * @author haotangyuan
 */
@Component
open class LinkStatsSaveProducer(private val stringRedisTemplate: StringRedisTemplate) {
    /** 发送延迟消费短链接统计 */
    open fun send(producerMap: Map<String, String>) {
        stringRedisTemplate.opsForStream<String, String>().add(SHORT_LINK_STATS_STREAM_TOPIC_KEY, producerMap)
    }
}
