package dev.haotangyuan.shortlink.vo

/**
 * 短链接访问网络监控响应参数
 *
 * @author haotangyuan
 */
open class LinkStatsNetworkVO(
    /**
     * 统计
     */
    open var cnt: Int? = null,

    /**
     * 访问网络
     */
    open var network: String? = null,

    /**
     * 占比
     */
    open var ratio: Double? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinkStatsNetworkVO || !other.canEqual(this)) return false
        return cnt == other.cnt &&
            network == other.network &&
            java.util.Objects.equals(ratio, other.ratio)
    }

    protected open fun canEqual(other: Any?): Boolean = other is LinkStatsNetworkVO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (cnt?.hashCode() ?: 43)
        result = result * 59 + (network?.hashCode() ?: 43)
        result = result * 59 + (ratio?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "LinkStatsNetworkVO(cnt=$cnt, network=$network, ratio=$ratio)"

    companion object {
        @JvmStatic
        fun builder(): LinkStatsNetworkVOBuilder = LinkStatsNetworkVOBuilder()
    }

    open class LinkStatsNetworkVOBuilder internal constructor() {
        private var cnt: Int? = null
        private var network: String? = null
        private var ratio: Double? = null

        open fun cnt(cnt: Int?): LinkStatsNetworkVOBuilder = apply { this.cnt = cnt }

        open fun network(network: String?): LinkStatsNetworkVOBuilder = apply { this.network = network }

        open fun ratio(ratio: Double?): LinkStatsNetworkVOBuilder = apply { this.ratio = ratio }

        open fun build(): LinkStatsNetworkVO = LinkStatsNetworkVO(cnt, network, ratio)

        override fun toString(): String =
            "LinkStatsNetworkVO.LinkStatsNetworkVOBuilder(cnt=$cnt, network=$network, ratio=$ratio)"
    }
}
