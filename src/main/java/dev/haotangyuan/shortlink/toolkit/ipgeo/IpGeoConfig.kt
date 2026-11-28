package dev.haotangyuan.shortlink.toolkit.ipgeo

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * IP 地理位置查询配置
 *
 * @author haotangyuan
 */
@Configuration
@EnableConfigurationProperties(IpGeoProps::class)
class IpGeoConfig {
    @Bean
    @ConditionalOnProperty(prefix = "short-link.stats.locale", name = ["provider"], havingValue = "local", matchIfMissing = true)
    fun localClient(p: IpGeoProps): IpGeoClient = LocalClient((p.local ?: throw NullPointerException()).dbPath)

    @Bean
    @ConditionalOnProperty(prefix = "short-link.stats.locale", name = ["provider"], havingValue = "amap", matchIfMissing = true)
    fun AmapClient(p: IpGeoProps): IpGeoClient {
        val amap = p.amap ?: throw NullPointerException()
        return AmapClient(amap.key, amap.endpoint, amap.timeout)
    }
}
