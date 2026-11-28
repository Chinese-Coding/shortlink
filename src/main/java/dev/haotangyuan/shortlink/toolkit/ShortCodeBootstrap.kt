package dev.haotangyuan.shortlink.toolkit

import jakarta.annotation.PostConstruct
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component

/**
 * 启动引导：由 Spring 容器在应用启动时执行一次，初始化短码生成器。
 *
 * @author haotangyuan
 */
@Component
class ShortCodeBootstrap(
    private val stringRedisTemplate: StringRedisTemplate,
    private val shortCodeProps: ShortCodeProps,
) {
    @PostConstruct
    fun init() {
        println("preload the ShortCodeGenerator!")
        ShortCodeUtil.init(stringRedisTemplate, shortCodeProps)
    }
}
