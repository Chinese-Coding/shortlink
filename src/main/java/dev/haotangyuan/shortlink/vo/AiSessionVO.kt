package dev.haotangyuan.shortlink.vo

import java.util.Date

/**
 * AI 会话列表返回对象
 */
open class AiSessionVO(
    /**
     * 会话 UUID
     */
    open var sessionId: String? = null,

    /**
     * 会话标题
     */
    open var title: String? = null,

    /**
     * 创建时间
     */
    open var createTime: Date? = null,

    /**
     * 更新时间（最后一条消息时间）
     */
    open var updateTime: Date? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AiSessionVO || !other.canEqual(this)) return false
        return sessionId == other.sessionId &&
            title == other.title &&
            createTime == other.createTime &&
            updateTime == other.updateTime
    }

    protected open fun canEqual(other: Any?): Boolean = other is AiSessionVO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (sessionId?.hashCode() ?: 43)
        result = result * 59 + (title?.hashCode() ?: 43)
        result = result * 59 + (createTime?.hashCode() ?: 43)
        result = result * 59 + (updateTime?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "AiSessionVO(sessionId=$sessionId, title=$title, createTime=$createTime, updateTime=$updateTime)"

    companion object {
        @JvmStatic
        fun builder(): AiSessionVOBuilder = AiSessionVOBuilder()
    }

    open class AiSessionVOBuilder internal constructor() {
        private var sessionId: String? = null
        private var title: String? = null
        private var createTime: Date? = null
        private var updateTime: Date? = null

        open fun sessionId(sessionId: String?): AiSessionVOBuilder = apply { this.sessionId = sessionId }

        open fun title(title: String?): AiSessionVOBuilder = apply { this.title = title }

        open fun createTime(createTime: Date?): AiSessionVOBuilder = apply { this.createTime = createTime }

        open fun updateTime(updateTime: Date?): AiSessionVOBuilder = apply { this.updateTime = updateTime }

        open fun build(): AiSessionVO = AiSessionVO(sessionId, title, createTime, updateTime)

        override fun toString(): String =
            "AiSessionVO.AiSessionVOBuilder(sessionId=$sessionId, title=$title, createTime=$createTime, " +
            "updateTime=$updateTime)"
    }
}
