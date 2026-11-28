package dev.haotangyuan.shortlink.common.config

import dev.haotangyuan.shortlink.common.web.ApiTokenAuthFilter
import dev.haotangyuan.shortlink.common.web.UserFlowRiskControlFilter
import dev.haotangyuan.shortlink.common.web.UserTransmitFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.StringRedisTemplate

/**
 * 用户配置自动装配
 *
 * @author haotangyuan
 */
@Configuration
class UserConfiguration {
    /** 用户信息传递过滤器 */
    @Bean
    fun globalUserTransmitFilter(
        stringRedisTemplate: StringRedisTemplate,
        @Value("\${short-link.session-ttl-minutes:30}") sessionTtlMinutes: Int,
    ): FilterRegistrationBean<UserTransmitFilter> = FilterRegistrationBean<UserTransmitFilter>().apply {
        setFilter(UserTransmitFilter(stringRedisTemplate, sessionTtlMinutes))
        // 仅拦截管理端路由，避免误伤核心与跳转
        addUrlPatterns("/api/short-link/admin/*")
        order = 0
    }

    /** Core API Token 过滤器 */
    @Bean
    fun coreApiTokenAuthFilter(stringRedisTemplate: StringRedisTemplate): FilterRegistrationBean<ApiTokenAuthFilter> =
        FilterRegistrationBean<ApiTokenAuthFilter>().apply {
            setFilter(ApiTokenAuthFilter(stringRedisTemplate))
            addUrlPatterns("/api/short-link/v1/*")
            order = 1
        }

    /** 用户操作流量风控过滤器 */
    @Bean
    @ConditionalOnProperty(name = ["short-link.flow-limit.enable"], havingValue = "true")
    fun globalUserFlowRiskControlFilter(
        stringRedisTemplate: StringRedisTemplate,
        userFlowRiskControlConfiguration: UserFlowRiskControlConfiguration,
    ): FilterRegistrationBean<UserFlowRiskControlFilter> = FilterRegistrationBean<UserFlowRiskControlFilter>().apply {
        setFilter(UserFlowRiskControlFilter(stringRedisTemplate, userFlowRiskControlConfiguration))
        addUrlPatterns("/api/short-link/*")
        order = 10
    }
}
