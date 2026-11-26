package dev.haotangyuan.shortlink.dto.biz

import com.alibaba.fastjson2.annotation.JSONField

/**
 * 用户信息实体
 *
 * @author haotangyuan
 */
open class UserInfoDTO(
    /**
     * 用户 ID
     */
    @field:JSONField(name = "id")
    open var userId: String? = null,

    /**
     * 用户名
     */
    open var username: String? = null,

    /**
     * 真实姓名
     */
    open var realName: String? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserInfoDTO || !other.canEqual(this)) return false
        return userId == other.userId &&
            username == other.username &&
            realName == other.realName
    }

    protected open fun canEqual(other: Any?): Boolean = other is UserInfoDTO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (userId?.hashCode() ?: 43)
        result = result * 59 + (username?.hashCode() ?: 43)
        result = result * 59 + (realName?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "UserInfoDTO(userId=$userId, username=$username, realName=$realName)"

    companion object {
        @JvmStatic
        fun builder(): UserInfoDTOBuilder = UserInfoDTOBuilder()
    }

    open class UserInfoDTOBuilder internal constructor() {
        private var userId: String? = null
        private var username: String? = null
        private var realName: String? = null

        open fun userId(userId: String?): UserInfoDTOBuilder = apply { this.userId = userId }

        open fun username(username: String?): UserInfoDTOBuilder = apply { this.username = username }

        open fun realName(realName: String?): UserInfoDTOBuilder = apply { this.realName = realName }

        open fun build(): UserInfoDTO = UserInfoDTO(userId, username, realName)

        override fun toString(): String =
            "UserInfoDTO.UserInfoDTOBuilder(userId=$userId, username=$username, realName=$realName)"
    }
}
