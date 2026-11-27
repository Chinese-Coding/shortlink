package dev.haotangyuan.shortlink.toolkit.ipgeo

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * IP 地理位置查询配置
 *
 * @author haotangyuan
 */
@ConfigurationProperties(prefix = "short-link.stats.locale")
open class IpGeoProps {
    /**
     * IP 地理位置查询服务提供商，支持 local（本地库）和 amap（高德地图）
     */
    open var provider: String? = "local"

    /**
     * 本地库配置
     */
    open var local: Local? = Local()

    /**
     * 高德地图配置
     */
    open var amap: Amap? = Amap()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is IpGeoProps || !other.canEqual(this)) return false
        return provider == other.provider &&
            local == other.local &&
            amap == other.amap
    }

    protected open fun canEqual(other: Any?): Boolean = other is IpGeoProps

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (provider?.hashCode() ?: 43)
        result = result * 59 + (local?.hashCode() ?: 43)
        result = result * 59 + (amap?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "IpGeoProps(provider=$provider, local=$local, amap=$amap)"

    open class Local {
        open var dbPath: String? = null

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Local || !other.canEqual(this)) return false
            return dbPath == other.dbPath
        }

        protected open fun canEqual(other: Any?): Boolean = other is Local

        override fun hashCode(): Int = 59 + (dbPath?.hashCode() ?: 43)

        override fun toString(): String =
            "IpGeoProps.Local(dbPath=$dbPath)"
    }

    open class Amap {
        open var key: String? = null
        open var endpoint: String? = null
        open var timeout: Int = 0

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Amap || !other.canEqual(this)) return false
            return key == other.key &&
                endpoint == other.endpoint &&
                timeout == other.timeout
        }

        protected open fun canEqual(other: Any?): Boolean = other is Amap

        override fun hashCode(): Int {
            var result = 1
            result = result * 59 + (key?.hashCode() ?: 43)
            result = result * 59 + (endpoint?.hashCode() ?: 43)
            result = result * 59 + timeout
            return result
        }

        override fun toString(): String =
            "IpGeoProps.Amap(key=$key, endpoint=$endpoint, timeout=$timeout)"
    }
}
