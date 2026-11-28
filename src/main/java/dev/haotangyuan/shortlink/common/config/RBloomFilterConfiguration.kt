package dev.haotangyuan.shortlink.common.config

import org.redisson.api.RBloomFilter
import org.redisson.api.RedissonClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author haotangyuan
 */
@Configuration
class RBloomFilterConfiguration {

    /** 防止用户注册查询数据库的布隆过滤器 */
    @Bean
    fun userRegisterCachePenetrationBloomFilter(redissonClient: RedissonClient): RBloomFilter<String> =
        redissonClient.getBloomFilter<String>("userRegisterCachePenetrationBloomFilter").apply {
            tryInit(100_000_000L, 0.001)
        }

    /** 防止短链接创建查询数据库的布隆过滤器 */
    @Bean
    fun shortUriCreateCachePenetrationBloomFilter(redissonClient: RedissonClient): RBloomFilter<String> =
        redissonClient.getBloomFilter<String>("shortUriCreateCachePenetrationBloomFilter").apply {
            tryInit(100_000_000L, 0.001)
        }
}
