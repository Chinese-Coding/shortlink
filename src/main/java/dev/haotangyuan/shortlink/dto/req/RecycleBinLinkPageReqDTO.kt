package dev.haotangyuan.shortlink.dto.req

/**
 * 分页查询回收站请求参数
 *
 * @author haotangyuan
 */
open class RecycleBinLinkPageReqDTO : PageReqDTO() {

    /**
     * 分组列表
     */
    open var gidList: MutableList<String>? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RecycleBinLinkPageReqDTO || !other.canEqual(this)) return false
        return gidList == other.gidList
    }

    override fun canEqual(other: Any?): Boolean = other is RecycleBinLinkPageReqDTO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (gidList?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "RecycleBinLinkPageReqDTO(gidList=$gidList)"
}
