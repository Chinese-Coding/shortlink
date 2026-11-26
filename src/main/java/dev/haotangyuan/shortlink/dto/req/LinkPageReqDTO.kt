package dev.haotangyuan.shortlink.dto.req

/**
 * 短链接分页请求参数
 *
 * @author haotangyuan
 */
open class LinkPageReqDTO : PageReqDTO() {

    /**
     * 分组标识
     */
    open var gid: String? = null

    /**
     * 搜索关键词
     */
    open var keyword: String? = null

    /**
     * 排序标识
     */
    open var orderTag: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinkPageReqDTO || !other.canEqual(this)) return false
        return gid == other.gid &&
            keyword == other.keyword &&
            orderTag == other.orderTag
    }

    override fun canEqual(other: Any?): Boolean = other is LinkPageReqDTO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (gid?.hashCode() ?: 43)
        result = result * 59 + (keyword?.hashCode() ?: 43)
        result = result * 59 + (orderTag?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "LinkPageReqDTO(gid=$gid, keyword=$keyword, orderTag=$orderTag)"
}
