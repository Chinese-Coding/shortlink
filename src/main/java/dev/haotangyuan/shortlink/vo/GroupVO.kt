package dev.haotangyuan.shortlink.vo

/**
 * 短链接分组返回实体
 *
 * @author haotangyuan
 */
open class GroupVO {

    /**
     * 分组标识
     */
    open var gid: String? = null

    /**
     * 分组名称
     */
    open var name: String? = null

    /**
     * 分组排序
     */
    open var sortOrder: Int? = null

    /**
     * 分组下短链接数量
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
        if (other !is GroupVO || !other.canEqual(this)) return false
        return gid == other.gid &&
            name == other.name &&
            sortOrder == other.sortOrder &&
            linkCount == other.linkCount &&
            totalPv == other.totalPv &&
            todayPv == other.todayPv
    }

    protected open fun canEqual(other: Any?): Boolean = other is GroupVO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (gid?.hashCode() ?: 43)
        result = result * 59 + (name?.hashCode() ?: 43)
        result = result * 59 + (sortOrder?.hashCode() ?: 43)
        result = result * 59 + (linkCount?.hashCode() ?: 43)
        result = result * 59 + (totalPv?.hashCode() ?: 43)
        result = result * 59 + (todayPv?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "GroupVO(gid=$gid, name=$name, sortOrder=$sortOrder, " +
            "linkCount=$linkCount, totalPv=$totalPv, todayPv=$todayPv)"
}
