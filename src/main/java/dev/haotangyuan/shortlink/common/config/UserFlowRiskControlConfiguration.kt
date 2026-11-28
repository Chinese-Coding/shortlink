package dev.haotangyuan.shortlink.common.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * 用户操作流量风控配置文件
 *
 * @author haotangyuan
 */
@Component
@ConfigurationProperties(prefix = "short-link.flow-limit")
class UserFlowRiskControlConfiguration {
    /**
     * 是否开启用户流量风控验证
     */
    var enable: Boolean? = null

    /**
     * 流量风控时间窗口，单位：秒
     */
    var timeWindow: String? = null

    /**
     * 流量风控时间窗口内可访问次数
     */
    var maxAccessCount: Long? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserFlowRiskControlConfiguration || !other.canEqual(this)) return false
        return enable == other.enable &&
            timeWindow == other.timeWindow &&
            maxAccessCount == other.maxAccessCount
    }

    protected fun canEqual(other: Any?): Boolean = other is UserFlowRiskControlConfiguration

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (enable?.hashCode() ?: 43)
        result = result * 59 + (timeWindow?.hashCode() ?: 43)
        result = result * 59 + (maxAccessCount?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "UserFlowRiskControlConfiguration(enable=$enable, timeWindow=$timeWindow, maxAccessCount=$maxAccessCount)"
}
