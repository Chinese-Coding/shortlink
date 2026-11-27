package dev.haotangyuan.shortlink.common.convention.exception

import dev.haotangyuan.shortlink.common.convention.errorcode.IErrorCode
import org.springframework.util.StringUtils

/**
 * 抽象项目中三类异常体系，客户端异常、服务端异常以及远程服务调用异常
 *
 * @author: haotangyuan
 * @see ClientException
 * @see ServiceException
 * @see RemoteException
 */
abstract class AbstractException(
    message: String?,
    throwable: Throwable?,
    errorCode: IErrorCode,
) : RuntimeException(message, throwable) {

    // Java 版同时有 public 字段和 Lombok 生成的 getter，两者都保留。
    @JvmField
    val errorCode: String = errorCode.code()

    @JvmField
    val errorMessage: String = message?.takeIf { StringUtils.hasLength(it) } ?: errorCode.message()

    open fun getErrorCode(): String = errorCode

    open fun getErrorMessage(): String = errorMessage
}
