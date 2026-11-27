package dev.haotangyuan.shortlink.toolkit.ipgeo

/**
 * IP 地理位置信息
 *
 * @author haotangyuan
 */
open class GeoInfo(
    /**
     * 国家
     */
    open var country: String? = null,

    /**
     * 省份
     */
    open var province: String? = null,

    /**
     * 城市
     */
    open var city: String? = null,

    /**
     * 城市编码
     */
    open var adcode: String? = null,

    /**
     * 运营商
     */
    open var isp: String? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GeoInfo || !other.canEqual(this)) return false
        return country == other.country &&
            province == other.province &&
            city == other.city &&
            adcode == other.adcode &&
            isp == other.isp
    }

    protected open fun canEqual(other: Any?): Boolean = other is GeoInfo

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (country?.hashCode() ?: 43)
        result = result * 59 + (province?.hashCode() ?: 43)
        result = result * 59 + (city?.hashCode() ?: 43)
        result = result * 59 + (adcode?.hashCode() ?: 43)
        result = result * 59 + (isp?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "GeoInfo(country=$country, province=$province, city=$city, adcode=$adcode, isp=$isp)"

    companion object {
        @JvmStatic
        fun unknown(): GeoInfo = GeoInfo("Unknown", "Unknown", "Unknown", "Unknown", "Unknown")

        @JvmStatic
        fun builder(): GeoInfoBuilder = GeoInfoBuilder()
    }

    open class GeoInfoBuilder internal constructor() {
        private var country: String? = null
        private var province: String? = null
        private var city: String? = null
        private var adcode: String? = null
        private var isp: String? = null

        open fun country(country: String?): GeoInfoBuilder = apply { this.country = country }

        open fun province(province: String?): GeoInfoBuilder = apply { this.province = province }

        open fun city(city: String?): GeoInfoBuilder = apply { this.city = city }

        open fun adcode(adcode: String?): GeoInfoBuilder = apply { this.adcode = adcode }

        open fun isp(isp: String?): GeoInfoBuilder = apply { this.isp = isp }

        open fun build(): GeoInfo = GeoInfo(country, province, city, adcode, isp)

        override fun toString(): String =
            "GeoInfo.GeoInfoBuilder(country=$country, province=$province, city=$city, " +
            "adcode=$adcode, isp=$isp)"
    }
}
