package dev.haotangyuan.shortlink.vo

import java.util.Date

/**
 * AI 消息返回对象
 */
open class AiMessageVO(
    /**
     * 角色: user / assistant
     */
    open var role: String? = null,

    /**
     * 消息文本内容
     */
    open var content: String? = null,

    /**
     * 创建时间
     */
    open var createTime: Date? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AiMessageVO || !other.canEqual(this)) return false
        return role == other.role &&
            content == other.content &&
            createTime == other.createTime
    }

    protected open fun canEqual(other: Any?): Boolean = other is AiMessageVO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (role?.hashCode() ?: 43)
        result = result * 59 + (content?.hashCode() ?: 43)
        result = result * 59 + (createTime?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "AiMessageVO(role=$role, content=$content, createTime=$createTime)"

    companion object {
        @JvmStatic
        fun builder(): AiMessageVOBuilder = AiMessageVOBuilder()
    }

    open class AiMessageVOBuilder internal constructor() {
        private var role: String? = null
        private var content: String? = null
        private var createTime: Date? = null

        open fun role(role: String?): AiMessageVOBuilder = apply { this.role = role }

        open fun content(content: String?): AiMessageVOBuilder = apply { this.content = content }

        open fun createTime(createTime: Date?): AiMessageVOBuilder = apply { this.createTime = createTime }

        open fun build(): AiMessageVO = AiMessageVO(role, content, createTime)

        override fun toString(): String =
            "AiMessageVO.AiMessageVOBuilder(role=$role, content=$content, createTime=$createTime)"
    }
}
