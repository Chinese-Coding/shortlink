package dev.haotangyuan.shortlink.common.convention.exception

import dev.haotangyuan.shortlink.common.convention.errorcode.BaseErrorCode
import dev.haotangyuan.shortlink.common.convention.errorcode.IErrorCode

/**
 * 服务端异常
 *
 * @author: haotangyuan
 */
open class ServiceException(
    message: String?,
    throwable: Throwable?,
    errorCode: IErrorCode,
) : AbstractException(message ?: errorCode.message(), throwable, errorCode) {

    constructor(message: String?) : this(message, null, BaseErrorCode.SERVICE_ERROR)

    constructor(errorCode: IErrorCode) : this(null, errorCode)

    constructor(message: String?, errorCode: IErrorCode) : this(message, null, errorCode)

    override fun toString(): String = "ServiceException{code='$errorCode',message='$errorMessage'}"
}
