package dev.haotangyuan.shortlink.dto.req

/**
 * AI 对话请求参数
 */
open class AiChatReqDTO {

    open var message: String? = null

    open var sessionId: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AiChatReqDTO || !other.canEqual(this)) return false
        return message == other.message &&
            sessionId == other.sessionId
    }

    protected open fun canEqual(other: Any?): Boolean = other is AiChatReqDTO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (message?.hashCode() ?: 43)
        result = result * 59 + (sessionId?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "AiChatReqDTO(message=$message, sessionId=$sessionId)"
}
