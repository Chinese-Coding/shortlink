package dev.haotangyuan.shortlink.dto.req

/**
 * 短链接分组排序请求参数
 *
 * @author haotangyuan
 */
open class GroupSortReqDTO {

    /**
     * 分组名
     */
    open var gid: String? = null

    /**
     * 排序
     */
    open var sortOrder: Int? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GroupSortReqDTO || !other.canEqual(this)) return false
        return gid == other.gid &&
            sortOrder == other.sortOrder
    }

    protected open fun canEqual(other: Any?): Boolean = other is GroupSortReqDTO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (gid?.hashCode() ?: 43)
        result = result * 59 + (sortOrder?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "GroupSortReqDTO(gid=$gid, sortOrder=$sortOrder)"
}
