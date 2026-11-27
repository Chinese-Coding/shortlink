package dev.haotangyuan.shortlink.controller.admin

import cn.hutool.core.bean.BeanUtil
import dev.haotangyuan.shortlink.common.convention.result.Result
import dev.haotangyuan.shortlink.common.convention.result.Results
import dev.haotangyuan.shortlink.dto.req.UserLoginReqDTO
import dev.haotangyuan.shortlink.dto.req.UserRegisterReqDTO
import dev.haotangyuan.shortlink.dto.req.UserUpdateReqDTO
import dev.haotangyuan.shortlink.vo.UserActualVO
import dev.haotangyuan.shortlink.vo.UserLoginVO
import dev.haotangyuan.shortlink.vo.UserVO
import dev.haotangyuan.shortlink.service.UserService
import org.springframework.web.bind.annotation.*

/**
 * 用户管理控制层
 *
 * @author haotangyuan
 */
@RestController
open class UserAdminController(private val userService: UserService) {

    /**
     * 根据用户名查找用户
     */
    @GetMapping("/api/short-link/admin/v1/user/{username}")
    open fun getUserByUsername(@PathVariable("username") username: String): Result<UserVO> =
        Results.success(userService.getByUsername(username))

    /**
     * 根据用户名查找用户无脱敏
     */
    @GetMapping("/api/short-link/admin/v1/actual/user/{username}")
    open fun getActualUserByUsername(@PathVariable("username") username: String): Result<UserActualVO> =
        Results.success(BeanUtil.toBean(userService.getByUsername(username), UserActualVO::class.java))

    /**
     * 查看用户名是否存在
     */
    @GetMapping("/api/short-link/admin/v1/user/exists")
    open fun existsByUsername(@RequestParam("username") username: String): Result<Boolean?> =
        Results.success(userService.existsByUsername(username))

    /**
     * 用户注册
     */
    @PostMapping("/api/short-link/admin/v1/user")
    open fun register(@RequestBody userRegisterReqDTO: UserRegisterReqDTO): Result<Void> {
        userService.register(userRegisterReqDTO)
        return Results.success()
    }

    /**
     * 修改信息
     */
    @PutMapping("/api/short-link/admin/v1/user")
    open fun updateUser(@RequestBody userUpdateReqDTO: UserUpdateReqDTO): Result<Void> {
        userService.updateByUsername(userUpdateReqDTO)
        return Results.success()
    }

    /**
     * 用户登录
     */
    @PostMapping("/api/short-link/admin/v1/user/login")
    open fun login(@RequestBody userLoginReqDTO: UserLoginReqDTO): Result<UserLoginVO> =
        Results.success(userService.login(userLoginReqDTO))

    /**
     * 检查用户是否登录
     */
    @PostMapping("/api/short-link/admin/v1/user/check-login")
    open fun checkLogin(
        @RequestParam("username") username: String,
        @RequestParam("token") token: String,
    ): Result<Boolean?> =
        Results.success(userService.checkLogin(username, token))

    /**
     * 退出登录
     */
    @DeleteMapping("/api/short-link/admin/v1/user/logout")
    open fun logout(@RequestParam("username") username: String, @RequestParam("token") token: String): Result<Void> {
        userService.logout(username, token)
        return Results.success()
    }
}
