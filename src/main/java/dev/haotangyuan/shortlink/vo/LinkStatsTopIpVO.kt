package dev.haotangyuan.shortlink.vo

/**
 * 短链接高频访问 IP 监控响应参数
 *
 * @author haotangyuan
 */
open class LinkStatsTopIpVO(
    /**
     * 统计
     */
    open var cnt: Int? = null,

    /**
     * IP
     */
    open var ip: String? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinkStatsTopIpVO || !other.canEqual(this)) return false
        return cnt == other.cnt &&
            ip == other.ip
    }

    protected open fun canEqual(other: Any?): Boolean = other is LinkStatsTopIpVO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (cnt?.hashCode() ?: 43)
        result = result * 59 + (ip?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "LinkStatsTopIpVO(cnt=$cnt, ip=$ip)"

    companion object {
        @JvmStatic
        fun builder(): LinkStatsTopIpVOBuilder = LinkStatsTopIpVOBuilder()
    }

    open class LinkStatsTopIpVOBuilder internal constructor() {
        private var cnt: Int? = null
        private var ip: String? = null

        open fun cnt(cnt: Int?): LinkStatsTopIpVOBuilder = apply { this.cnt = cnt }

        open fun ip(ip: String?): LinkStatsTopIpVOBuilder = apply { this.ip = ip }

        open fun build(): LinkStatsTopIpVO = LinkStatsTopIpVO(cnt, ip)

        override fun toString(): String =
            "LinkStatsTopIpVO.LinkStatsTopIpVOBuilder(cnt=$cnt, ip=$ip)"
    }
}
