package dev.haotangyuan.shortlink.common.convention.errorcode

/**
 * 平台错误码
 *
 * @author haotangyuan
 */
interface IErrorCode {

    /**
     * 错误码
     */
    fun code(): String

    /**
     * 错误信息
     */
    fun message(): String
}
