package dev.haotangyuan.shortlink.common.biz.user

/**
 * 分组归属校验（Verifier）
 * 仅校验 gid 是否属于当前登录用户（UserContext.username）
 */
interface GroupOwnershipVerifier {

    /**
     * 断言 gid 属于当前用户
     *
     * @param gid 分组标识
     */
    fun assertOwnedByCurrentUser(gid: String)

    /**
     * 断言一组 gid 均属于当前用户
     *
     * @param gids 分组标识列表
     */
    fun assertAllOwnedByCurrentUser(gids: List<String>)
}
