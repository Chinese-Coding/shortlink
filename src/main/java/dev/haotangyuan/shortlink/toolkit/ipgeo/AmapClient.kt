package dev.haotangyuan.shortlink.toolkit.ipgeo

import cn.hutool.http.HttpUtil
import com.alibaba.fastjson2.JSON
import org.slf4j.LoggerFactory

/**
 * 高德地图 IP 地理位置查询客户端
 *
 * @author haotangyuan
 */
open class AmapClient(
    private val key: String?,
    private val endpoint: String?,
    private val timeout: Int,
) : IpGeoClient {
    override fun query(ip: String?): GeoInfo {
        return try {
            val parameters = hashMapOf<String, Any?>("key" to key, "ip" to ip)
            val response = JSON.parseObject(HttpUtil.get(endpoint, parameters, timeout), AmapResp::class.java)
            if (response?.status != "1") return GeoInfo.unknown()

            val adcode = normalize(response.adcode)
            GeoInfo(
                country = if (adcode != null && AREA_CODE.matches(adcode)) "中国" else null,
                province = normalize(response.province),
                city = normalize(response.city),
                adcode = adcode,
            )
        } catch (exception: Exception) {
            log.warn("Failed to query Amap IP geolocation, ip={}, reason={}", ip, exception.message)
            GeoInfo.unknown()
        }
    }

    private class AmapResp {
        var status: String? = null
        var info: String? = null
        var province: String? = null
        var city: String? = null
        var adcode: String? = null
    }

    companion object {
        private val log = LoggerFactory.getLogger(AmapClient::class.java)
        private val AREA_CODE = Regex("\\d{6}")

        private fun normalize(value: String?): String? {
            val trimmed = value?.trim { it <= ' ' } ?: return null
            return if (trimmed.isEmpty() || trimmed == "[]") "Unknown" else trimmed
        }
    }
}
