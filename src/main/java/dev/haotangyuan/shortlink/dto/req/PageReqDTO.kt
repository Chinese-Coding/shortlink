package dev.haotangyuan.shortlink.dto.req

/**
 * 分页请求基类
 *
 * @author haotangyuan
 */
open class PageReqDTO {

    open var current: Long = 1L
        get() = field.coerceAtLeast(1L)
    open var size: Long = 10L
        get() = field.coerceIn(1L, 100L)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PageReqDTO || !other.canEqual(this)) return false
        return current == other.current &&
            size == other.size
    }

    protected open fun canEqual(other: Any?): Boolean = other is PageReqDTO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + current.hashCode()
        result = result * 59 + size.hashCode()
        return result
    }

    override fun toString(): String =
        "PageReqDTO(current=$current, size=$size)"
}
