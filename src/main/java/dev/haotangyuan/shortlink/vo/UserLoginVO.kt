package dev.haotangyuan.shortlink.vo

/**
 * 用户登录接口返回响应
 *
 * @author haotangyuan
 */
open class UserLoginVO(
    /**
     * 用户 token
     */
    open var token: String?,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserLoginVO || !other.canEqual(this)) return false
        return token == other.token
    }

    protected open fun canEqual(other: Any?): Boolean = other is UserLoginVO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (token?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "UserLoginVO(token=$token)"
}
