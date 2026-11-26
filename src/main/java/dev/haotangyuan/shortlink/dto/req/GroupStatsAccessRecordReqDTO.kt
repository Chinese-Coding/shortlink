package dev.haotangyuan.shortlink.dto.req

/**
 * 分组短链接监控访问记录请求参数
 *
 * @author haotangyuan
 */
open class GroupStatsAccessRecordReqDTO : PageReqDTO() {

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GroupStatsAccessRecordReqDTO || !other.canEqual(this)) return false
        return gid == other.gid &&
            startDate == other.startDate &&
            endDate == other.endDate
    }

    override fun canEqual(other: Any?): Boolean = other is GroupStatsAccessRecordReqDTO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (gid?.hashCode() ?: 43)
        result = result * 59 + (startDate?.hashCode() ?: 43)
        result = result * 59 + (endDate?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "GroupStatsAccessRecordReqDTO(gid=$gid, startDate=$startDate, endDate=$endDate)"
}
