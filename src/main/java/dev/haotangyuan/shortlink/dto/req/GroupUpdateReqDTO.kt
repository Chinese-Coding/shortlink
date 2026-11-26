package dev.haotangyuan.shortlink.dto.req

/**
 * 短链接分组修改请求参数
 *
 * @author haotangyuan
 */
open class GroupUpdateReqDTO {

    /**
     * 分组标识
     */
    open var gid: String? = null

    /**
     * 分组名
     */
    open var name: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GroupUpdateReqDTO || !other.canEqual(this)) return false
        return gid == other.gid &&
            name == other.name
    }

    protected open fun canEqual(other: Any?): Boolean = other is GroupUpdateReqDTO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (gid?.hashCode() ?: 43)
        result = result * 59 + (name?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "GroupUpdateReqDTO(gid=$gid, name=$name)"
}
