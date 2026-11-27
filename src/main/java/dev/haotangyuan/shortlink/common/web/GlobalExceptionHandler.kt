package dev.haotangyuan.shortlink.common.web

import dev.haotangyuan.shortlink.common.convention.errorcode.BaseErrorCode
import dev.haotangyuan.shortlink.common.convention.exception.AbstractException
import dev.haotangyuan.shortlink.common.convention.exception.ClientException
import dev.haotangyuan.shortlink.common.convention.result.Result
import dev.haotangyuan.shortlink.common.convention.result.Results
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * 全局异常处理器
 *
 * @author haotangyuan
 */
@RestControllerAdvice
open class GlobalExceptionHandler {
    /** 拦截参数验证异常 */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    open fun validExceptionHandler(request: HttpServletRequest, ex: MethodArgumentNotValidException): Result<*> {
        val message = ex.bindingResult.fieldErrors.firstOrNull()?.defaultMessage.orEmpty()
        log.warn("[{}] {} [ex] {}", request.method, request.requestURL.toString(), message)
        return Results.failure(BaseErrorCode.CLIENT_ERROR.code(), message)
    }

    /** 拦截应用内抛出的异常 */
    @ExceptionHandler(AbstractException::class)
    open fun abstractException(request: HttpServletRequest, ex: AbstractException): Result<*> {
        val url = request.requestURL.toString()
        val cause = ex.cause
        when {
            cause != null -> log.error("[{}] {} [ex] {}", request.method, url, ex.toString(), cause)
            ex is ClientException -> log.warn("[{}] {} [ex] {}", request.method, url, ex.toString())
            else -> log.error("[{}] {} [ex] {}", request.method, url, ex.toString())
        }
        return Results.failure(ex)
    }

    /** 拦截未捕获异常 */
    @ExceptionHandler(Throwable::class)
    open fun defaultErrorHandler(request: HttpServletRequest, throwable: Throwable): Result<*> {
        log.error("[{}] {} ", request.method, request.requestURL.toString(), throwable)
        return Results.failure()
    }

    companion object {
        private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }
}
