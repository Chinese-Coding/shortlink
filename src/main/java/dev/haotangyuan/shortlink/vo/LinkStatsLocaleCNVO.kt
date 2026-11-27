package dev.haotangyuan.shortlink.vo

/**
 * 短链接地区监控响应参数
 *
 * @author haotangyuan
 */
open class LinkStatsLocaleCNVO(
    /**
     * 统计
     */
    open var cnt: Int? = null,

    /**
     * 地区
     */
    open var locale: String? = null,

    /**
     * 占比
     */
    open var ratio: Double? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinkStatsLocaleCNVO || !other.canEqual(this)) return false
        return cnt == other.cnt &&
            locale == other.locale &&
            java.util.Objects.equals(ratio, other.ratio)
    }

    protected open fun canEqual(other: Any?): Boolean = other is LinkStatsLocaleCNVO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (cnt?.hashCode() ?: 43)
        result = result * 59 + (locale?.hashCode() ?: 43)
        result = result * 59 + (ratio?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "LinkStatsLocaleCNVO(cnt=$cnt, locale=$locale, ratio=$ratio)"

    companion object {
        @JvmStatic
        fun builder(): LinkStatsLocaleCNVOBuilder = LinkStatsLocaleCNVOBuilder()
    }

    open class LinkStatsLocaleCNVOBuilder internal constructor() {
        private var cnt: Int? = null
        private var locale: String? = null
        private var ratio: Double? = null

        open fun cnt(cnt: Int?): LinkStatsLocaleCNVOBuilder = apply { this.cnt = cnt }

        open fun locale(locale: String?): LinkStatsLocaleCNVOBuilder = apply { this.locale = locale }

        open fun ratio(ratio: Double?): LinkStatsLocaleCNVOBuilder = apply { this.ratio = ratio }

        open fun build(): LinkStatsLocaleCNVO = LinkStatsLocaleCNVO(cnt, locale, ratio)

        override fun toString(): String =
            "LinkStatsLocaleCNVO.LinkStatsLocaleCNVOBuilder(cnt=$cnt, locale=$locale, ratio=$ratio)"
    }
}
