package dev.haotangyuan.shortlink.common.convention.exception

import dev.haotangyuan.shortlink.common.convention.errorcode.BaseErrorCode
import dev.haotangyuan.shortlink.common.convention.errorcode.IErrorCode

/**
 * 客户端异常
 *
 * @author: haotangyuan
 */
open class ClientException(
    message: String?,
    throwable: Throwable?,
    errorCode: IErrorCode,
) : AbstractException(message, throwable, errorCode) {

    constructor(errorCode: IErrorCode) : this(null, null, errorCode)

    constructor(message: String?) : this(message, null, BaseErrorCode.CLIENT_ERROR)

    constructor(message: String?, errorCode: IErrorCode) : this(message, null, errorCode)

    override fun toString(): String = "ClientException{code='$errorCode',message='$errorMessage'}"
}
