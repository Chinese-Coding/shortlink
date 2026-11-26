package dev.haotangyuan.shortlink.service

import dev.haotangyuan.shortlink.dto.req.UserLoginReqDTO
import dev.haotangyuan.shortlink.dto.req.UserRegisterReqDTO
import dev.haotangyuan.shortlink.dto.req.UserUpdateReqDTO
import dev.haotangyuan.shortlink.vo.UserLoginVO
import dev.haotangyuan.shortlink.vo.UserVO

/**
 * 用户接口层
 *
 * @author haotangyuan
 */
interface UserService {
    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return UserVO 用户返回实体
     */
    fun getByUsername(username: String): UserVO

    /**
     * 查看用户名是否存在
     *
     * @param username 用户名
     * @return Boolean
     */
    fun existsByUsername(username: String): Boolean?

    /**
     * 注册用户
     *
     * @param userRegisterReqDTO
     * @return void
     */
    fun register(userRegisterReqDTO: UserRegisterReqDTO)

    /**
     * 修改用户信息
     *
     * @param userUpdateReqDTO
     * @return void
     */
    fun updateByUsername(userUpdateReqDTO: UserUpdateReqDTO)

    /**
     * 用户登录
     *
     * @param userLoginReqDTO
     * @return UserLoginVO
     */
    fun login(userLoginReqDTO: UserLoginReqDTO): UserLoginVO

    /**
     * 检查用户是否登录
     *
     * @param username
     * @param token
     * @return Boolean
     */
    fun checkLogin(username: String, token: String): Boolean?

    /**
     * 退出登录
     *
     * @param username
     * @param token
     * @return
     */
    fun logout(username: String, token: String)
}
