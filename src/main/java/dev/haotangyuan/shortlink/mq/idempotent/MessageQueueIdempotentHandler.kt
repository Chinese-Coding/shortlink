package dev.haotangyuan.shortlink.mq.idempotent

import dev.haotangyuan.shortlink.common.constant.RedisKeyConstant.Companion.IDEMPOTENT_KEY_PREFIX
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

/**
 * 消息队列幂等处理器
 *
 * @author haotangyuan
 */
@Component
open class MessageQueueIdempotentHandler(private val stringRedisTemplate: StringRedisTemplate) {
    /**
     * 判断消息是否已被处理过（同时尝试标记为处理中）
     *
     * @param messageId 消息唯一标识
     * @return true-消息已被处理过（重复消费）, false-首次处理
     */
    open fun isProcessed(messageId: String?): Boolean {
        val key = String.format(IDEMPOTENT_KEY_PREFIX, messageId)
        // setIfAbsent 返回 false 表示 key 已存在（已被处理过）
        return stringRedisTemplate.opsForValue().setIfAbsent(key, "0", Duration.ofMinutes(2)) == false
    }

    /** 判断消息消费流程是否执行完成 */
    open fun isAccomplish(messageId: String?): Boolean {
        val key = String.format(IDEMPOTENT_KEY_PREFIX, messageId)
        return stringRedisTemplate.opsForValue().get(key) == "1"
    }

    /**
     * 设置消息流程执行完成
     * 第一次被获取到的最坏时间为 minIdleMs + 巡检间隔，也就是 2.5 分钟
     */
    open fun setAccomplish(messageId: String?) {
        val key = String.format(IDEMPOTENT_KEY_PREFIX, messageId)
        stringRedisTemplate.opsForValue().set(key, "1", Duration.ofMinutes(4))
    }

    /** 释放幂等标识（消息处理失败时调用） */
    open fun release(messageId: String?) {
        stringRedisTemplate.delete(String.format(IDEMPOTENT_KEY_PREFIX, messageId))
    }
}
