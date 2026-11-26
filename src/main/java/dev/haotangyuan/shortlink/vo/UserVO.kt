package dev.haotangyuan.shortlink.vo

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import dev.haotangyuan.shortlink.common.serialize.PhoneDesensitizationSerializer

/**
 * 用户返回参数响应
 *
 * @author haotangyuan
 */
open class UserVO {

    /**
     * ID
     */
    @field:JsonSerialize(using = ToStringSerializer::class)
    open var id: Long? = null

    /**
     * 用户名
     */
    open var username: String? = null

    /**
     * 真实姓名
     */
    open var realName: String? = null

    /**
     * 手机号
     */
    @field:JsonSerialize(using = PhoneDesensitizationSerializer::class)
    open var phone: String? = null

    /**
     * 邮箱
     */
    open var mail: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserVO || !other.canEqual(this)) return false
        return id == other.id &&
            username == other.username &&
            realName == other.realName &&
            phone == other.phone &&
            mail == other.mail
    }

    protected open fun canEqual(other: Any?): Boolean = other is UserVO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (id?.hashCode() ?: 43)
        result = result * 59 + (username?.hashCode() ?: 43)
        result = result * 59 + (realName?.hashCode() ?: 43)
        result = result * 59 + (phone?.hashCode() ?: 43)
        result = result * 59 + (mail?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "UserVO(id=$id, username=$username, realName=$realName, phone=$phone, mail=$mail)"
}
