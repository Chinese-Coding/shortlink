package dev.haotangyuan.shortlink.dto.req

/**
 * 用户注册请求参数
 *
 * @author haotangyuan
 */
open class UserRegisterReqDTO {

    /**
     * 用户名
     */
    open var username: String? = null

    /**
     * 密码
     */
    open var password: String? = null

    /**
     * 真实姓名
     */
    open var realName: String? = null

    /**
     * 手机号
     */
    open var phone: String? = null

    /**
     * 邮箱
     */
    open var mail: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserRegisterReqDTO || !other.canEqual(this)) return false
        return username == other.username &&
            password == other.password &&
            realName == other.realName &&
            phone == other.phone &&
            mail == other.mail
    }

    protected open fun canEqual(other: Any?): Boolean = other is UserRegisterReqDTO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (username?.hashCode() ?: 43)
        result = result * 59 + (password?.hashCode() ?: 43)
        result = result * 59 + (realName?.hashCode() ?: 43)
        result = result * 59 + (phone?.hashCode() ?: 43)
        result = result * 59 + (mail?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "UserRegisterReqDTO(username=$username, password=$password, realName=$realName, phone=$phone, mail=$mail)"
}
