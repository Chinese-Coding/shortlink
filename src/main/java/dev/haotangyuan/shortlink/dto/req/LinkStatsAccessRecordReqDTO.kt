package dev.haotangyuan.shortlink.dto.req

/**
 * 短链接访问记录请求参数
 *
 * @author haotangyuan
 */
open class LinkStatsAccessRecordReqDTO : PageReqDTO() {

    /**
     * 完整短链接
     */
    open var fullShortUrl: String? = null

    /**
     * 分组标识
     */
    open var gid: String? = null

    /**
     * 开始日期
     */
    open var startDate: String? = null

    /**
     * 结束日期
     */
    open var endDate: String? = null

    /**
     * 启用标识 0：启用 1：未启用
     */
    open var enableStatus: Int? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinkStatsAccessRecordReqDTO || !other.canEqual(this)) return false
        return fullShortUrl == other.fullShortUrl &&
            gid == other.gid &&
            startDate == other.startDate &&
            endDate == other.endDate &&
            enableStatus == other.enableStatus
    }

    override fun canEqual(other: Any?): Boolean = other is LinkStatsAccessRecordReqDTO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (fullShortUrl?.hashCode() ?: 43)
        result = result * 59 + (gid?.hashCode() ?: 43)
        result = result * 59 + (startDate?.hashCode() ?: 43)
        result = result * 59 + (endDate?.hashCode() ?: 43)
        result = result * 59 + (enableStatus?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "LinkStatsAccessRecordReqDTO(fullShortUrl=$fullShortUrl, gid=$gid, startDate=$startDate, " +
            "endDate=$endDate, enableStatus=$enableStatus)"
}
