package dev.haotangyuan.shortlink.dto.req

/**
 * 新增短链接分组请求参数
 *
 * @author haotangyuan
 */
open class GroupSaveReqDTO {

    /**
     * 分组名称
     */
    open var name: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GroupSaveReqDTO || !other.canEqual(this)) return false
        return name == other.name
    }

    protected open fun canEqual(other: Any?): Boolean = other is GroupSaveReqDTO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (name?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "GroupSaveReqDTO(name=$name)"
}
