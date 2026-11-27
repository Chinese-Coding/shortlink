package dev.haotangyuan.shortlink.dto.req

/**
 * 用户登录请求参数
 *
 * @author haotangyuan
 */
open class UserLoginReqDTO {

    /**
     * 用户名
     */
    open var username: String? = null

    /**
     * 密码
     */
    open var password: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserLoginReqDTO || !other.canEqual(this)) return false
        return username == other.username &&
            password == other.password
    }

    protected open fun canEqual(other: Any?): Boolean = other is UserLoginReqDTO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (username?.hashCode() ?: 43)
        result = result * 59 + (password?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "UserLoginReqDTO(username=$username, password=$password)"
}
