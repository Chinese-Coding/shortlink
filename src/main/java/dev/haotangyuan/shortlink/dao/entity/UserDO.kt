package dev.haotangyuan.shortlink.dao.entity

import com.baomidou.mybatisplus.annotation.TableName
import dev.haotangyuan.shortlink.common.database.BaseDO

/**
 * 用户持久层实体
 *
 * @author haotangyuan
 */
@TableName("t_user")
open class UserDO : BaseDO() {

    /**
     * ID
     */
    open var id: Long? = null

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

    /**
     * 注销时间戳
     */
    open var deletionTime: Long? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserDO || !other.canEqual(this)) return false
        return id == other.id &&
            username == other.username &&
            password == other.password &&
            realName == other.realName &&
            phone == other.phone &&
            mail == other.mail &&
            deletionTime == other.deletionTime
    }

    override fun canEqual(other: Any?): Boolean = other is UserDO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (id?.hashCode() ?: 43)
        result = result * 59 + (username?.hashCode() ?: 43)
        result = result * 59 + (password?.hashCode() ?: 43)
        result = result * 59 + (realName?.hashCode() ?: 43)
        result = result * 59 + (phone?.hashCode() ?: 43)
        result = result * 59 + (mail?.hashCode() ?: 43)
        result = result * 59 + (deletionTime?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "UserDO(id=$id, username=$username, password=$password, realName=$realName, " +
            "phone=$phone, mail=$mail, deletionTime=$deletionTime)"
}
