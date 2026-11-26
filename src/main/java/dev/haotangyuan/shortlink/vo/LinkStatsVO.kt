package dev.haotangyuan.shortlink.vo

/**
 * 短链接监控响应参数
 *
 * @author haotangyuan
 */
open class LinkStatsVO(
    /**
     * 访问量
     */
    open var pv: Int? = null,

    /**
     * 独立访客数
     */
    open var uv: Int? = null,

    /**
     * 独立IP数
     */
    open var uip: Int? = null,

    /**
     * 基础访问详情
     */
    open var daily: MutableList<LinkStatsAccessDailyVO>? = null,

    /**
     * 地区访问详情
     */
    open var localeCnStats: MutableList<LinkStatsLocaleCNVO>? = null,

    /**
     * 小时访问详情
     */
    open var hourStats: MutableList<Int>? = null,

    /**
     * 高频访问IP详情
     */
    open var topIpStats: MutableList<LinkStatsTopIpVO>? = null,

    /**
     * 一周访问详情
     */
    open var weekdayStats: MutableList<Int>? = null,

    /**
     * 浏览器访问详情
     */
    open var browserStats: MutableList<LinkStatsBrowserVO>? = null,

    /**
     * 操作系统访问详情
     */
    open var osStats: MutableList<LinkStatsOsVO>? = null,

    /**
     * 访客访问类型详情
     */
    open var uvTypeStats: MutableList<LinkStatsUvVO>? = null,

    /**
     * 访问设备类型详情
     */
    open var deviceStats: MutableList<LinkStatsDeviceVO>? = null,

    /**
     * 访问网络类型详情
     */
    open var networkStats: MutableList<LinkStatsNetworkVO>? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinkStatsVO || !other.canEqual(this)) return false
        return pv == other.pv &&
            uv == other.uv &&
            uip == other.uip &&
            daily == other.daily &&
            localeCnStats == other.localeCnStats &&
            hourStats == other.hourStats &&
            topIpStats == other.topIpStats &&
            weekdayStats == other.weekdayStats &&
            browserStats == other.browserStats &&
            osStats == other.osStats &&
            uvTypeStats == other.uvTypeStats &&
            deviceStats == other.deviceStats &&
            networkStats == other.networkStats
    }

    protected open fun canEqual(other: Any?): Boolean = other is LinkStatsVO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (pv?.hashCode() ?: 43)
        result = result * 59 + (uv?.hashCode() ?: 43)
        result = result * 59 + (uip?.hashCode() ?: 43)
        result = result * 59 + (daily?.hashCode() ?: 43)
        result = result * 59 + (localeCnStats?.hashCode() ?: 43)
        result = result * 59 + (hourStats?.hashCode() ?: 43)
        result = result * 59 + (topIpStats?.hashCode() ?: 43)
        result = result * 59 + (weekdayStats?.hashCode() ?: 43)
        result = result * 59 + (browserStats?.hashCode() ?: 43)
        result = result * 59 + (osStats?.hashCode() ?: 43)
        result = result * 59 + (uvTypeStats?.hashCode() ?: 43)
        result = result * 59 + (deviceStats?.hashCode() ?: 43)
        result = result * 59 + (networkStats?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "LinkStatsVO(pv=$pv, uv=$uv, uip=$uip, daily=$daily, localeCnStats=$localeCnStats, hourStats=$hourStats, topIpStats=$topIpStats, " +
            "weekdayStats=$weekdayStats, browserStats=$browserStats, osStats=$osStats, uvTypeStats=$uvTypeStats, deviceStats=$deviceStats, networkStats=$networkStats)"

    companion object {
        @JvmStatic
        fun builder(): LinkStatsVOBuilder = LinkStatsVOBuilder()
    }

    open class LinkStatsVOBuilder internal constructor() {
        private var pv: Int? = null
        private var uv: Int? = null
        private var uip: Int? = null
        private var daily: MutableList<LinkStatsAccessDailyVO>? = null
        private var localeCnStats: MutableList<LinkStatsLocaleCNVO>? = null
        private var hourStats: MutableList<Int>? = null
        private var topIpStats: MutableList<LinkStatsTopIpVO>? = null
        private var weekdayStats: MutableList<Int>? = null
        private var browserStats: MutableList<LinkStatsBrowserVO>? = null
        private var osStats: MutableList<LinkStatsOsVO>? = null
        private var uvTypeStats: MutableList<LinkStatsUvVO>? = null
        private var deviceStats: MutableList<LinkStatsDeviceVO>? = null
        private var networkStats: MutableList<LinkStatsNetworkVO>? = null

        open fun pv(pv: Int?): LinkStatsVOBuilder = apply { this.pv = pv }

        open fun uv(uv: Int?): LinkStatsVOBuilder = apply { this.uv = uv }

        open fun uip(uip: Int?): LinkStatsVOBuilder = apply { this.uip = uip }

        open fun daily(daily: MutableList<LinkStatsAccessDailyVO>?): LinkStatsVOBuilder = apply { this.daily = daily }

        open fun localeCnStats(localeCnStats: MutableList<LinkStatsLocaleCNVO>?): LinkStatsVOBuilder = apply { this.localeCnStats = localeCnStats }

        open fun hourStats(hourStats: MutableList<Int>?): LinkStatsVOBuilder = apply { this.hourStats = hourStats }

        open fun topIpStats(topIpStats: MutableList<LinkStatsTopIpVO>?): LinkStatsVOBuilder = apply { this.topIpStats = topIpStats }

        open fun weekdayStats(weekdayStats: MutableList<Int>?): LinkStatsVOBuilder = apply { this.weekdayStats = weekdayStats }

        open fun browserStats(browserStats: MutableList<LinkStatsBrowserVO>?): LinkStatsVOBuilder = apply { this.browserStats = browserStats }

        open fun osStats(osStats: MutableList<LinkStatsOsVO>?): LinkStatsVOBuilder = apply { this.osStats = osStats }

        open fun uvTypeStats(uvTypeStats: MutableList<LinkStatsUvVO>?): LinkStatsVOBuilder = apply { this.uvTypeStats = uvTypeStats }

        open fun deviceStats(deviceStats: MutableList<LinkStatsDeviceVO>?): LinkStatsVOBuilder = apply { this.deviceStats = deviceStats }

        open fun networkStats(networkStats: MutableList<LinkStatsNetworkVO>?): LinkStatsVOBuilder = apply { this.networkStats = networkStats }

        open fun build(): LinkStatsVO = LinkStatsVO(
            pv,
            uv,
            uip,
            daily,
            localeCnStats,
            hourStats,
            topIpStats,
            weekdayStats,
            browserStats,
            osStats,
            uvTypeStats,
            deviceStats,
            networkStats,
        )

        override fun toString(): String =
            "LinkStatsVO.LinkStatsVOBuilder(pv=$pv, uv=$uv, uip=$uip, " +
            "daily=$daily, localeCnStats=$localeCnStats, hourStats=$hourStats, " +
            "topIpStats=$topIpStats, weekdayStats=$weekdayStats, browserStats=$browserStats, " +
            "osStats=$osStats, uvTypeStats=$uvTypeStats, deviceStats=$deviceStats, " +
            "networkStats=$networkStats)"
    }
}
