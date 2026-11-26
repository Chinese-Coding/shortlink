package dev.haotangyuan.shortlink.dao.entity

import com.baomidou.mybatisplus.annotation.TableName
import dev.haotangyuan.shortlink.common.database.BaseDO

/**
 * AI 对话消息持久层实体
 */
@TableName("t_ai_message")
open class AiMessageDO(
    /**
     * id
     */
    open var id: Long? = null,

    /**
     * 所属会话 UUID
     */
    open var sessionId: String? = null,

    /**
     * 角色: user / assistant
     */
    open var role: String? = null,

    /**
     * 消息文本内容
     */
    open var content: String? = null,
) : BaseDO() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AiMessageDO || !other.canEqual(this)) return false
        return id == other.id &&
            sessionId == other.sessionId &&
            role == other.role &&
            content == other.content
    }

    override fun canEqual(other: Any?): Boolean = other is AiMessageDO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (id?.hashCode() ?: 43)
        result = result * 59 + (sessionId?.hashCode() ?: 43)
        result = result * 59 + (role?.hashCode() ?: 43)
        result = result * 59 + (content?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "AiMessageDO(id=$id, sessionId=$sessionId, role=$role, content=$content)"

    companion object {
        @JvmStatic
        fun builder(): AiMessageDOBuilder = AiMessageDOBuilder()
    }

    open class AiMessageDOBuilder internal constructor() {
        private var id: Long? = null
        private var sessionId: String? = null
        private var role: String? = null
        private var content: String? = null

        open fun id(id: Long?): AiMessageDOBuilder = apply { this.id = id }

        open fun sessionId(sessionId: String?): AiMessageDOBuilder = apply { this.sessionId = sessionId }

        open fun role(role: String?): AiMessageDOBuilder = apply { this.role = role }

        open fun content(content: String?): AiMessageDOBuilder = apply { this.content = content }

        open fun build(): AiMessageDO = AiMessageDO(id, sessionId, role, content)

        override fun toString(): String =
            "AiMessageDO.AiMessageDOBuilder(id=$id, sessionId=$sessionId, role=$role, content=$content)"
    }
}
