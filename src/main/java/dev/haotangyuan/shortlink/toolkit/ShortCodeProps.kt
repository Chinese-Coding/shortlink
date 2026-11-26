package dev.haotangyuan.shortlink.toolkit

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.util.Objects

/**
 * 短码生成器配置（application.yaml 可直接配置）
 *
 * @author haotangyuan
 */
@Configuration
@ConfigurationProperties(prefix = "short-link.shortcode")
open class ShortCodeProps {
    /**
     * 固定短码长度（默认 6）
     */
    open var length: Int? = 6

    /**
     * 号段步长：每次从 Redis 预取的数量（默认 10000）
     */
    open var segmentStep: Long? = 10_000L

    /**
     * 预取阈值占比：剩余量 <= step*ratio 时触发预取（默认 0.2）
     */
    open var prefetchRatio: Double? = 0.2

    /**
     * 仿射置换参数 a（需与 62 与 31 互素，且为奇数）。为空则用默认值
     */
    open var a: String? = null

    /**
     * 仿射置换参数 b（需 0<=b<62^length）。为空则用默认值
     */
    open var b: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ShortCodeProps || !other.canEqual(this)) return false
        return length == other.length &&
            segmentStep == other.segmentStep &&
            Objects.equals(prefetchRatio, other.prefetchRatio) &&
            a == other.a &&
            b == other.b
    }

    protected open fun canEqual(other: Any?): Boolean = other is ShortCodeProps

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (length?.hashCode() ?: 43)
        result = result * 59 + (segmentStep?.hashCode() ?: 43)
        result = result * 59 + (prefetchRatio?.hashCode() ?: 43)
        result = result * 59 + (a?.hashCode() ?: 43)
        result = result * 59 + (b?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "ShortCodeProps(length=$length, segmentStep=$segmentStep, prefetchRatio=$prefetchRatio, a=$a, b=$b)"
}
