package dev.haotangyuan.shortlink.service

import dev.haotangyuan.shortlink.dto.req.TokenCreateReqDTO
import dev.haotangyuan.shortlink.vo.TokenVO

/**
 * @author haotangyuan
 */
interface TokenService {
    /**
     * 创建 API 访问令牌，返回明文 token（仅创建时可见）
     */
    fun createToken(req: TokenCreateReqDTO): String

    /**
     * 列出当前用户的所有令牌（脱敏显示 token）
     */
    fun listTokens(): List<TokenVO>

    /**
     * 吊销（删除）令牌
     */
    fun deleteToken(id: Long?)

    /**
     * 启用/禁用令牌
     */
    fun updateStatus(id: Long?, enable: Boolean?)
}
