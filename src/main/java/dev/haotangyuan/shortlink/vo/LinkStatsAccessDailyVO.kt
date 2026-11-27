package dev.haotangyuan.shortlink.vo

/**
 * 短链接基础访问监控响应参数
 *
 * @author haotangyuan
 */
open class LinkStatsAccessDailyVO(
    /**
     * 日期
     */
    open var date: String? = null,

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
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinkStatsAccessDailyVO || !other.canEqual(this)) return false
        return date == other.date &&
            pv == other.pv &&
            uv == other.uv &&
            uip == other.uip
    }

    protected open fun canEqual(other: Any?): Boolean = other is LinkStatsAccessDailyVO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (date?.hashCode() ?: 43)
        result = result * 59 + (pv?.hashCode() ?: 43)
        result = result * 59 + (uv?.hashCode() ?: 43)
        result = result * 59 + (uip?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "LinkStatsAccessDailyVO(date=$date, pv=$pv, uv=$uv, uip=$uip)"

    companion object {
        @JvmStatic
        fun builder(): LinkStatsAccessDailyVOBuilder = LinkStatsAccessDailyVOBuilder()
    }

    open class LinkStatsAccessDailyVOBuilder internal constructor() {
        private var date: String? = null
        private var pv: Int? = null
        private var uv: Int? = null
        private var uip: Int? = null

        open fun date(date: String?): LinkStatsAccessDailyVOBuilder = apply { this.date = date }

        open fun pv(pv: Int?): LinkStatsAccessDailyVOBuilder = apply { this.pv = pv }

        open fun uv(uv: Int?): LinkStatsAccessDailyVOBuilder = apply { this.uv = uv }

        open fun uip(uip: Int?): LinkStatsAccessDailyVOBuilder = apply { this.uip = uip }

        open fun build(): LinkStatsAccessDailyVO = LinkStatsAccessDailyVO(date, pv, uv, uip)

        override fun toString(): String =
            "LinkStatsAccessDailyVO.LinkStatsAccessDailyVOBuilder(date=$date, pv=$pv, uv=$uv, uip=$uip)"
    }
}
