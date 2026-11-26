package dev.haotangyuan.shortlink.dto.req

import com.fasterxml.jackson.annotation.JsonFormat

import java.util.Date

/**
 * 创建 API 访问令牌请求
 */
open class TokenCreateReqDTO {

    /**
     * 令牌名称
     */
    open var name: String? = null

    /**
     * 描述
     */
    open var describe: String? = null

    /**
     * 有效期，null 表示永不过期
     */
    @field:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    open var validDate: Date? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TokenCreateReqDTO || !other.canEqual(this)) return false
        return name == other.name &&
            describe == other.describe &&
            validDate == other.validDate
    }

    protected open fun canEqual(other: Any?): Boolean = other is TokenCreateReqDTO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (name?.hashCode() ?: 43)
        result = result * 59 + (describe?.hashCode() ?: 43)
        result = result * 59 + (validDate?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "TokenCreateReqDTO(name=$name, describe=$describe, validDate=$validDate)"
}
