package dev.haotangyuan.shortlink.common.convention.result

import dev.haotangyuan.shortlink.common.convention.errorcode.BaseErrorCode
import dev.haotangyuan.shortlink.common.convention.exception.AbstractException

/**
 * 全局返回对象构造器
 *
 * @author: haotangyuan
 */
object Results {

    /**
     * 构造成功响应
     */
    @JvmStatic
    fun success(): Result<Void> =
        Result<Void>()
            .setCode(Result.SUCCESS_CODE)

    /**
     * 构造带返回数据的成功响应
     */
    @JvmStatic
    fun <T> success(data: T): Result<T> =
        Result<T>()
            .setCode(Result.SUCCESS_CODE)
            .setData(data)

    /**
     * 构建服务端失败响应
     */
    @JvmStatic
    fun failure(): Result<Void> =
        Result<Void>()
            .setCode(BaseErrorCode.SERVICE_ERROR.code())
            .setMessage(BaseErrorCode.SERVICE_ERROR.message())

    /**
     * 通过 [AbstractException] 构建失败响应
     */
    @JvmStatic
    fun failure(abstractException: AbstractException): Result<Void> =
        Result<Void>()
            .setCode(abstractException.errorCode)
            .setMessage(abstractException.errorMessage)

    /**
     * 通过 errorCode、errorMessage 构建失败响应
     */
    @JvmStatic
    fun failure(errorCode: String?, errorMessage: String?): Result<Void> =
        Result<Void>()
            .setCode(errorCode)
            .setMessage(errorMessage)
}
