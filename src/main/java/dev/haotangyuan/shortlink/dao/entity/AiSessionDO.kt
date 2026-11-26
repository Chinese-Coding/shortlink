package dev.haotangyuan.shortlink.dao.entity

import com.baomidou.mybatisplus.annotation.TableName
import dev.haotangyuan.shortlink.common.database.BaseDO

/**
 * AI 对话会话持久层实体
 */
@TableName("t_ai_session")
open class AiSessionDO(
    /**
     * id
     */
    open var id: Long? = null,

    /**
     * UUID 会话标识（前端生成）
     */
    open var sessionId: String? = null,

    /**
     * 所属用户名
     */
    open var username: String? = null,

    /**
     * 会话标题（首条用户消息截断 30 字）
     */
    open var title: String? = null,
) : BaseDO() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AiSessionDO || !other.canEqual(this)) return false
        return id == other.id &&
            sessionId == other.sessionId &&
            username == other.username &&
            title == other.title
    }

    override fun canEqual(other: Any?): Boolean = other is AiSessionDO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (id?.hashCode() ?: 43)
        result = result * 59 + (sessionId?.hashCode() ?: 43)
        result = result * 59 + (username?.hashCode() ?: 43)
        result = result * 59 + (title?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "AiSessionDO(id=$id, sessionId=$sessionId, username=$username, title=$title)"

    companion object {
        @JvmStatic
        fun builder(): AiSessionDOBuilder = AiSessionDOBuilder()
    }

    open class AiSessionDOBuilder internal constructor() {
        private var id: Long? = null
        private var sessionId: String? = null
        private var username: String? = null
        private var title: String? = null

        open fun id(id: Long?): AiSessionDOBuilder = apply { this.id = id }

        open fun sessionId(sessionId: String?): AiSessionDOBuilder = apply { this.sessionId = sessionId }

        open fun username(username: String?): AiSessionDOBuilder = apply { this.username = username }

        open fun title(title: String?): AiSessionDOBuilder = apply { this.title = title }

        open fun build(): AiSessionDO = AiSessionDO(id, sessionId, username, title)

        override fun toString(): String =
            "AiSessionDO.AiSessionDOBuilder(id=$id, sessionId=$sessionId, username=$username, title=$title)"
    }
}
