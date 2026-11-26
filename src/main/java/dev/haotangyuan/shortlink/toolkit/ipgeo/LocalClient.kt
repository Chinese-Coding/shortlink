package dev.haotangyuan.shortlink.toolkit.ipgeo

import org.lionsoul.ip2region.xdb.Searcher
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.util.FileCopyUtils

/**
 * IP 地理位置查询本地客户端(ip2region xdb, 全内存模式)
 *
 * @author haotangyuan
 */
open class LocalClient(dbPath: String?) : IpGeoClient {
    /** 完全基于内存的查询对象 */
    private val searcher: Searcher = try {
        val resource = ClassPathResource("ip2region.xdb")
        Searcher.newWithBuffer(FileCopyUtils.copyToByteArray(resource.inputStream))
    } catch (exception: Exception) {
        throw IllegalStateException("Init ip2region Searcher failed. dbPath=$dbPath", exception)
    }

    override fun query(ip: String?): GeoInfo {
        // 检查是否是 IPv6 地址
        if (ip?.contains(':') == true) return GeoInfo.unknown()

        return try {
            val parts = searcher.search(ip)?.split('|').orEmpty()
            GeoInfo(
                country = parts.getOrElse(0) { "Unknown" },
                province = parts.getOrElse(2) { "Unknown" },
                city = parts.getOrElse(3) { "Unknown" },
                isp = parts.getOrElse(4) { "Unknown" },
            )
        } catch (exception: Exception) {
            log.warn("Failed to query local IP geolocation, ip={}", ip, exception)
            GeoInfo.unknown()
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(LocalClient::class.java)
    }
}
