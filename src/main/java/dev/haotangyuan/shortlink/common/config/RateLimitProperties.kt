package dev.haotangyuan.shortlink.common.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * 短链接系统限流配置文件
 *
 * @author haotangyuan
 */
@Component
@ConfigurationProperties(prefix = "short-link.rate-limit")
class RateLimitProperties {
    var create: DetailProperties? = DetailProperties()
    var redirect: DetailProperties? = DetailProperties()
    var stats: DetailProperties? = DetailProperties()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RateLimitProperties || !other.canEqual(this)) return false
        return create == other.create &&
            redirect == other.redirect &&
            stats == other.stats
    }

    protected fun canEqual(other: Any?): Boolean = other is RateLimitProperties

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (create?.hashCode() ?: 43)
        result = result * 59 + (redirect?.hashCode() ?: 43)
        result = result * 59 + (stats?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "RateLimitProperties(create=$create, redirect=$redirect, stats=$stats)"

    open class DetailProperties {
        /**
         * 是否开启限流
         */
        open var enable: Boolean? = true

        /**
         * 每秒钟允许通过的请求数
         */
        open var rps: Double = 100.0

        /**
         * 获取令牌超时时间，单位：毫秒
         */
        open var timeout: Int = 100

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is DetailProperties || !other.canEqual(this)) return false
            return enable == other.enable &&
                java.lang.Double.compare(rps, other.rps) == 0 &&
                timeout == other.timeout
        }

        protected open fun canEqual(other: Any?): Boolean = other is DetailProperties

        override fun hashCode(): Int {
            var result = 1
            result = result * 59 + (enable?.hashCode() ?: 43)
            result = result * 59 + rps.hashCode()
            result = result * 59 + timeout
            return result
        }

        override fun toString(): String =
            "RateLimitProperties.DetailProperties(enable=$enable, rps=$rps, timeout=$timeout)"
    }
}
