package dev.haotangyuan.shortlink.vo

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer

import java.util.Date

/**
 * API 访问令牌返回对象（列表）
 */
open class TokenVO(
    /**
     * 令牌 ID
     */
    @field:JsonSerialize(using = ToStringSerializer::class)
    open var id: Long? = null,

    /**
     * token 脱敏
     */
    open var tokenMasked: String? = null,

    /**
     * 令牌名称
     */
    open var name: String? = null,

    /**
     * 启用标识 0：启用 1：未启用
     */
    open var enableStatus: Int? = null,

    /**
     * 有效期（时间戳，单位毫秒），null 表示永不过期
     */
    open var validDate: Date? = null,

    /**
     * 描述
     */
    open var describe: String? = null,

    /**
     * 修改时间
     */
    open var updateTime: Date? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TokenVO || !other.canEqual(this)) return false
        return id == other.id &&
            tokenMasked == other.tokenMasked &&
            name == other.name &&
            enableStatus == other.enableStatus &&
            validDate == other.validDate &&
            describe == other.describe &&
            updateTime == other.updateTime
    }

    protected open fun canEqual(other: Any?): Boolean = other is TokenVO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (id?.hashCode() ?: 43)
        result = result * 59 + (tokenMasked?.hashCode() ?: 43)
        result = result * 59 + (name?.hashCode() ?: 43)
        result = result * 59 + (enableStatus?.hashCode() ?: 43)
        result = result * 59 + (validDate?.hashCode() ?: 43)
        result = result * 59 + (describe?.hashCode() ?: 43)
        result = result * 59 + (updateTime?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "TokenVO(id=$id, tokenMasked=$tokenMasked, name=$name, enableStatus=$enableStatus, " +
            "validDate=$validDate, describe=$describe, updateTime=$updateTime)"

    companion object {
        @JvmStatic
        fun builder(): TokenVOBuilder = TokenVOBuilder()
    }

    open class TokenVOBuilder internal constructor() {
        private var id: Long? = null
        private var tokenMasked: String? = null
        private var name: String? = null
        private var enableStatus: Int? = null
        private var validDate: Date? = null
        private var describe: String? = null
        private var updateTime: Date? = null

        open fun id(id: Long?): TokenVOBuilder = apply { this.id = id }

        open fun tokenMasked(tokenMasked: String?): TokenVOBuilder = apply { this.tokenMasked = tokenMasked }

        open fun name(name: String?): TokenVOBuilder = apply { this.name = name }

        open fun enableStatus(enableStatus: Int?): TokenVOBuilder = apply { this.enableStatus = enableStatus }

        open fun validDate(validDate: Date?): TokenVOBuilder = apply { this.validDate = validDate }

        open fun describe(describe: String?): TokenVOBuilder = apply { this.describe = describe }

        open fun updateTime(updateTime: Date?): TokenVOBuilder = apply { this.updateTime = updateTime }

        open fun build(): TokenVO = TokenVO(id, tokenMasked, name, enableStatus, validDate, describe, updateTime)

        override fun toString(): String =
            "TokenVO.TokenVOBuilder(id=$id, tokenMasked=$tokenMasked, name=$name, " +
            "enableStatus=$enableStatus, validDate=$validDate, describe=$describe, " +
            "updateTime=$updateTime)"
    }
}
