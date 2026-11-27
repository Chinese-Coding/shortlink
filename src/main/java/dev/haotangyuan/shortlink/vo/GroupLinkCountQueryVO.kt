package dev.haotangyuan.shortlink.vo

/**
 * 分组查询响应参数
 *
 * @author haotangyuan
 */
open class GroupLinkCountQueryVO {

    /**
     * 分组标识
     */
    open var gid: String? = null

    /**
     * 短链接数
     */
    open var linkCount: Int? = null

    /**
     * 累计访问量
     */
    open var totalPv: Long? = null

    /**
     * 今日访问量
     */
    open var todayPv: Long? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GroupLinkCountQueryVO || !other.canEqual(this)) return false
        return gid == other.gid &&
            linkCount == other.linkCount &&
            totalPv == other.totalPv &&
            todayPv == other.todayPv
    }

    protected open fun canEqual(other: Any?): Boolean = other is GroupLinkCountQueryVO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (gid?.hashCode() ?: 43)
        result = result * 59 + (linkCount?.hashCode() ?: 43)
        result = result * 59 + (totalPv?.hashCode() ?: 43)
        result = result * 59 + (todayPv?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "GroupLinkCountQueryVO(gid=$gid, linkCount=$linkCount, totalPv=$totalPv, todayPv=$todayPv)"
}
