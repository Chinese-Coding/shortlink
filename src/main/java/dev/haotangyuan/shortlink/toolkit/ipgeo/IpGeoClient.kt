package dev.haotangyuan.shortlink.toolkit.ipgeo

/**
 * IP 地理位置查询客户端
 *
 * @author haotangyuan
 */
interface IpGeoClient {

    /**
     * 查询 IP 的地理位置信息
     *
     * @param ip IP 地址
     * @return
     */
    fun query(ip: String?): GeoInfo
}
