package dev.haotangyuan.shortlink.vo

/**
 * 短链接访问设备监控响应参数
 *
 * @author haotangyuan
 */
open class LinkStatsDeviceVO(
    /**
     * 统计
     */
    open var cnt: Int? = null,

    /**
     * 设备类型
     */
    open var device: String? = null,

    /**
     * 占比
     */
    open var ratio: Double? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinkStatsDeviceVO || !other.canEqual(this)) return false
        return cnt == other.cnt &&
            device == other.device &&
            java.util.Objects.equals(ratio, other.ratio)
    }

    protected open fun canEqual(other: Any?): Boolean = other is LinkStatsDeviceVO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (cnt?.hashCode() ?: 43)
        result = result * 59 + (device?.hashCode() ?: 43)
        result = result * 59 + (ratio?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "LinkStatsDeviceVO(cnt=$cnt, device=$device, ratio=$ratio)"

    companion object {
        @JvmStatic
        fun builder(): LinkStatsDeviceVOBuilder = LinkStatsDeviceVOBuilder()
    }

    open class LinkStatsDeviceVOBuilder internal constructor() {
        private var cnt: Int? = null
        private var device: String? = null
        private var ratio: Double? = null

        open fun cnt(cnt: Int?): LinkStatsDeviceVOBuilder = apply { this.cnt = cnt }

        open fun device(device: String?): LinkStatsDeviceVOBuilder = apply { this.device = device }

        open fun ratio(ratio: Double?): LinkStatsDeviceVOBuilder = apply { this.ratio = ratio }

        open fun build(): LinkStatsDeviceVO = LinkStatsDeviceVO(cnt, device, ratio)

        override fun toString(): String =
            "LinkStatsDeviceVO.LinkStatsDeviceVOBuilder(cnt=$cnt, device=$device, ratio=$ratio)"
    }
}
