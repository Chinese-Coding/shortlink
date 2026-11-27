package dev.haotangyuan.shortlink.common.convention.exception

import dev.haotangyuan.shortlink.common.convention.errorcode.BaseErrorCode
import dev.haotangyuan.shortlink.common.convention.errorcode.IErrorCode

/**
 * 远程服务调用异常
 *
 * @author: haotangyuan
 */
open class RemoteException(
    message: String?,
    throwable: Throwable?,
    errorCode: IErrorCode,
) : AbstractException(message, throwable, errorCode) {

    constructor(message: String?) : this(message, null, BaseErrorCode.REMOTE_ERROR)

    constructor(message: String?, errorCode: IErrorCode) : this(message, null, errorCode)

    override fun toString(): String = "RemoteException{code='$errorCode',message='$errorMessage'}"
}
