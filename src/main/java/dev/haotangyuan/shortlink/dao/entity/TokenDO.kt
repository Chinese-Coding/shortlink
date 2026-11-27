package dev.haotangyuan.shortlink.dao.entity

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName
import dev.haotangyuan.shortlink.common.database.BaseDO

import java.util.Date

/**
 * API 访问令牌持久层实体
 */
@TableName("t_api_token")
open class TokenDO(
    /**
     * id
     */
    open var id: Long? = null,

    /**
     * 用户名
     */
    open var username: String? = null,

    /**
     * token 哈希（SHA-256 十六进制）
     */
    open var tokenHash: String? = null,

    /**
     * token 后四位（用于识别显示）
     */
    open var tokenLast4: String? = null,

    /**
     * 令牌名称
     */
    open var name: String? = null,

    /**
     * 启用标识 0：启用 1：未启用
     */
    open var enableStatus: Int? = null,

    /**
     * 有效期（时间戳），null 表示永不过期
     */
    open var validDate: Date? = null,

    /**
     * 描述
     */
    @field:TableField("\"describe\"")
    open var describe: String? = null,
) : BaseDO() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TokenDO || !other.canEqual(this)) return false
        return id == other.id &&
            username == other.username &&
            tokenHash == other.tokenHash &&
            tokenLast4 == other.tokenLast4 &&
            name == other.name &&
            enableStatus == other.enableStatus &&
            validDate == other.validDate &&
            describe == other.describe
    }

    override fun canEqual(other: Any?): Boolean = other is TokenDO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (id?.hashCode() ?: 43)
        result = result * 59 + (username?.hashCode() ?: 43)
        result = result * 59 + (tokenHash?.hashCode() ?: 43)
        result = result * 59 + (tokenLast4?.hashCode() ?: 43)
        result = result * 59 + (name?.hashCode() ?: 43)
        result = result * 59 + (enableStatus?.hashCode() ?: 43)
        result = result * 59 + (validDate?.hashCode() ?: 43)
        result = result * 59 + (describe?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "TokenDO(id=$id, username=$username, tokenHash=$tokenHash, tokenLast4=$tokenLast4, " +
            "name=$name, enableStatus=$enableStatus, validDate=$validDate, describe=$describe)"

    companion object {
        @JvmStatic
        fun builder(): TokenDOBuilder = TokenDOBuilder()
    }

    open class TokenDOBuilder internal constructor() {
        private var id: Long? = null
        private var username: String? = null
        private var tokenHash: String? = null
        private var tokenLast4: String? = null
        private var name: String? = null
        private var enableStatus: Int? = null
        private var validDate: Date? = null
        private var describe: String? = null

        open fun id(id: Long?): TokenDOBuilder = apply { this.id = id }

        open fun username(username: String?): TokenDOBuilder = apply { this.username = username }

        open fun tokenHash(tokenHash: String?): TokenDOBuilder = apply { this.tokenHash = tokenHash }

        open fun tokenLast4(tokenLast4: String?): TokenDOBuilder = apply { this.tokenLast4 = tokenLast4 }

        open fun name(name: String?): TokenDOBuilder = apply { this.name = name }

        open fun enableStatus(enableStatus: Int?): TokenDOBuilder = apply { this.enableStatus = enableStatus }

        open fun validDate(validDate: Date?): TokenDOBuilder = apply { this.validDate = validDate }

        open fun describe(describe: String?): TokenDOBuilder = apply { this.describe = describe }

        open fun build(): TokenDO = TokenDO(
            id,
            username,
            tokenHash,
            tokenLast4,
            name,
            enableStatus,
            validDate,
            describe,
        )

        override fun toString(): String =
            "TokenDO.TokenDOBuilder(id=$id, username=$username, tokenHash=$tokenHash, " +
            "tokenLast4=$tokenLast4, name=$name, enableStatus=$enableStatus, " +
            "validDate=$validDate, describe=$describe)"
    }
}
