package dev.haotangyuan.shortlink.vo

/**
 * 短链接操作系统监控响应参数
 *
 * @author haotangyuan
 */
open class LinkStatsOsVO(
    /**
     * 统计
     */
    open var cnt: Int? = null,

    /**
     * 操作系统
     */
    open var os: String? = null,

    /**
     * 占比
     */
    open var ratio: Double? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinkStatsOsVO || !other.canEqual(this)) return false
        return cnt == other.cnt &&
            os == other.os &&
            java.util.Objects.equals(ratio, other.ratio)
    }

    protected open fun canEqual(other: Any?): Boolean = other is LinkStatsOsVO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (cnt?.hashCode() ?: 43)
        result = result * 59 + (os?.hashCode() ?: 43)
        result = result * 59 + (ratio?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "LinkStatsOsVO(cnt=$cnt, os=$os, ratio=$ratio)"

    companion object {
        @JvmStatic
        fun builder(): LinkStatsOsVOBuilder = LinkStatsOsVOBuilder()
    }

    open class LinkStatsOsVOBuilder internal constructor() {
        private var cnt: Int? = null
        private var os: String? = null
        private var ratio: Double? = null

        open fun cnt(cnt: Int?): LinkStatsOsVOBuilder = apply { this.cnt = cnt }

        open fun os(os: String?): LinkStatsOsVOBuilder = apply { this.os = os }

        open fun ratio(ratio: Double?): LinkStatsOsVOBuilder = apply { this.ratio = ratio }

        open fun build(): LinkStatsOsVO = LinkStatsOsVO(cnt, os, ratio)

        override fun toString(): String =
            "LinkStatsOsVO.LinkStatsOsVOBuilder(cnt=$cnt, os=$os, ratio=$ratio)"
    }
}
