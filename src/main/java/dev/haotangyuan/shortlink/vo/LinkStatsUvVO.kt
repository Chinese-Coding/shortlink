package dev.haotangyuan.shortlink.vo

/**
 * 短链接访客监控响应参数
 *
 * @author haotangyuan
 */
open class LinkStatsUvVO(
    /**
     * 统计
     */
    open var cnt: Int? = null,

    /**
     * 访客类型
     */
    open var uvType: String? = null,

    /**
     * 占比
     */
    open var ratio: Double? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinkStatsUvVO || !other.canEqual(this)) return false
        return cnt == other.cnt &&
            uvType == other.uvType &&
            java.util.Objects.equals(ratio, other.ratio)
    }

    protected open fun canEqual(other: Any?): Boolean = other is LinkStatsUvVO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (cnt?.hashCode() ?: 43)
        result = result * 59 + (uvType?.hashCode() ?: 43)
        result = result * 59 + (ratio?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "LinkStatsUvVO(cnt=$cnt, uvType=$uvType, ratio=$ratio)"

    companion object {
        @JvmStatic
        fun builder(): LinkStatsUvVOBuilder = LinkStatsUvVOBuilder()
    }

    open class LinkStatsUvVOBuilder internal constructor() {
        private var cnt: Int? = null
        private var uvType: String? = null
        private var ratio: Double? = null

        open fun cnt(cnt: Int?): LinkStatsUvVOBuilder = apply { this.cnt = cnt }

        open fun uvType(uvType: String?): LinkStatsUvVOBuilder = apply { this.uvType = uvType }

        open fun ratio(ratio: Double?): LinkStatsUvVOBuilder = apply { this.ratio = ratio }

        open fun build(): LinkStatsUvVO = LinkStatsUvVO(cnt, uvType, ratio)

        override fun toString(): String =
            "LinkStatsUvVO.LinkStatsUvVOBuilder(cnt=$cnt, uvType=$uvType, ratio=$ratio)"
    }
}
