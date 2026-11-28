package dev.haotangyuan.shortlink.common.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * 跳转域名白名单配置文件
 *
 * @author haotangyuan
 */
@Component
@ConfigurationProperties(prefix = "short-link.goto-domain.white-list")
class GotoDomainWhiteListConfiguration {
    /**
     * 是否开启跳转原始链接域名白名单验证
     */
    var enable: Boolean? = null

    /**
     * 跳转原始域名白名单网站名称集合
     */
    var names: String? = null

    /**
     * 可跳转的原始链接域名
     */
    var details: MutableList<String>? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GotoDomainWhiteListConfiguration || !other.canEqual(this)) return false
        return enable == other.enable &&
            names == other.names &&
            details == other.details
    }

    protected fun canEqual(other: Any?): Boolean = other is GotoDomainWhiteListConfiguration

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (enable?.hashCode() ?: 43)
        result = result * 59 + (names?.hashCode() ?: 43)
        result = result * 59 + (details?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "GotoDomainWhiteListConfiguration(enable=$enable, names=$names, details=$details)"
}
