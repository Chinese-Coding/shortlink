package dev.haotangyuan.shortlink.service

import dev.haotangyuan.shortlink.vo.AiMessageVO
import dev.haotangyuan.shortlink.vo.AiSessionVO

/**
 * AI 会话管理服务
 */
interface AiSessionService {

    /**
     * 幂等创建会话（已存在则跳过）
     */
    fun getOrCreateSession(sessionId: String)

    /**
     * 获取当前用户的会话列表（按更新时间倒序）
     */
    fun listSessions(): List<AiSessionVO>

    /**
     * 获取指定会话的消息列表（按创建时间正序）
     */
    fun getSessionMessages(sessionId: String): List<AiMessageVO>

    /**
     * 保存一条消息
     */
    fun saveMessage(sessionId: String, role: String, content: String)

    /**
     * 更新会话标题
     */
    fun updateSessionTitle(sessionId: String, title: String)

    /**
     * 逻辑删除会话及其所有消息
     */
    fun deleteSession(sessionId: String)

    /**
     * 获取会话消息数量
     */
    fun getMessageCount(sessionId: String): Long
}
