package dev.haotangyuan.shortlink.common.convention.result

import java.io.Serial
import java.io.Serializable

/**
 * 全局返回对象
 *
 * @author: haotangyuan
 */
open class Result<T> : Serializable {

    // Lombok 的 @Accessors(chain = true) 让 setter 返回 this，Kotlin 属性的 setter 返回 Unit，
    // 所以字段保持私有，由下面显式的 getter/setter 提供与原来一致的 JVM 签名。

    /**
     * 返回码
     */
    private var code: String? = null

    /**
     * 返回消息
     */
    private var message: String? = null

    /**
     * 响应数据
     */
    private var data: T? = null

    /**
     * 请求ID
     */
    private var requestId: String? = null

    open fun isSuccess(): Boolean = SUCCESS_CODE == code

    open fun getCode(): String? = code

    open fun getMessage(): String? = message

    open fun getData(): T? = data

    open fun getRequestId(): String? = requestId

    open fun setCode(code: String?): Result<T> {
        this.code = code
        return this
    }

    open fun setMessage(message: String?): Result<T> {
        this.message = message
        return this
    }

    open fun setData(data: T?): Result<T> {
        this.data = data
        return this
    }

    open fun setRequestId(requestId: String?): Result<T> {
        this.requestId = requestId
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Result<*> || !other.canEqual(this)) return false
        return getCode() == other.getCode() &&
            getMessage() == other.getMessage() &&
            getData() == other.getData() &&
            getRequestId() == other.getRequestId()
    }

    protected open fun canEqual(other: Any?): Boolean = other is Result<*>

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (getCode()?.hashCode() ?: 43)
        result = result * 59 + (getMessage()?.hashCode() ?: 43)
        result = result * 59 + (getData()?.hashCode() ?: 43)
        result = result * 59 + (getRequestId()?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "Result(code=${getCode()}, message=${getMessage()}, data=${getData()}, requestId=${getRequestId()})"

    companion object {
        @Serial
        private const val serialVersionUID = 5679018624309023727L

        /**
         * 正确返回码
         */
        const val SUCCESS_CODE = "0"
    }
}
