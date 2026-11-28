package dev.haotangyuan.shortlink.common.config

import com.google.common.util.concurrent.RateLimiter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * 短链接系统限流配置文件
 *
 * @author haotangyuan
 */
@Configuration
class RateLimitConfiguration {

    @Bean("createRateLimiter")
    fun createRateLimiter(p: RateLimitProperties): RateLimiter = rateLimiter(p.create ?: throw NullPointerException())

    @Bean("redirectRateLimiter")
    fun redirectRateLimiter(p: RateLimitProperties): RateLimiter = rateLimiter(p.redirect ?: throw NullPointerException())

    @Bean("statsRateLimiter")
    fun statsRateLimiter(p: RateLimitProperties): RateLimiter = rateLimiter(p.stats ?: throw NullPointerException())

    private fun rateLimiter(detail: RateLimitProperties.DetailProperties): RateLimiter =
        RateLimiter.create(if (detail.enable == true) detail.rps else Double.MAX_VALUE)
}
