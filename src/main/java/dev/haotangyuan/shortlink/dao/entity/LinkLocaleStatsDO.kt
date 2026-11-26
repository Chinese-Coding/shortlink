package dev.haotangyuan.shortlink.dao.entity

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName
import dev.haotangyuan.shortlink.common.database.BaseDO

import java.util.Date

/**
 * 地区访问统计实体
 *
 * @author haotangyuan
 */
@TableName("t_link_locale_stats")
open class LinkLocaleStatsDO(
    /**
     * id
     */
    open var id: Long? = null,

    /**
     * 完整短链接
     */
    open var fullShortUrl: String? = null,

    /**
     * 日期
     */
    @field:TableField("stats_date")
    open var date: Date? = null,

    /**
     * 访问量
     */
    open var cnt: Int? = null,

    /**
     * 省份名称
     */
    open var province: String? = null,

    /**
     * 市名称
     */
    open var city: String? = null,

    /**
     * 城市编码
     */
    open var adcode: String? = null,

    /**
     * 国家标识
     */
    open var country: String? = null,
) : BaseDO() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinkLocaleStatsDO || !other.canEqual(this)) return false
        return id == other.id &&
            fullShortUrl == other.fullShortUrl &&
            date == other.date &&
            cnt == other.cnt &&
            province == other.province &&
            city == other.city &&
            adcode == other.adcode &&
            country == other.country
    }

    override fun canEqual(other: Any?): Boolean = other is LinkLocaleStatsDO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (id?.hashCode() ?: 43)
        result = result * 59 + (fullShortUrl?.hashCode() ?: 43)
        result = result * 59 + (date?.hashCode() ?: 43)
        result = result * 59 + (cnt?.hashCode() ?: 43)
        result = result * 59 + (province?.hashCode() ?: 43)
        result = result * 59 + (city?.hashCode() ?: 43)
        result = result * 59 + (adcode?.hashCode() ?: 43)
        result = result * 59 + (country?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "LinkLocaleStatsDO(id=$id, fullShortUrl=$fullShortUrl, date=$date, cnt=$cnt, " +
            "province=$province, city=$city, adcode=$adcode, country=$country)"

    companion object {
        @JvmStatic
        fun builder(): LinkLocaleStatsDOBuilder = LinkLocaleStatsDOBuilder()
    }

    open class LinkLocaleStatsDOBuilder internal constructor() {
        private var id: Long? = null
        private var fullShortUrl: String? = null
        private var date: Date? = null
        private var cnt: Int? = null
        private var province: String? = null
        private var city: String? = null
        private var adcode: String? = null
        private var country: String? = null

        open fun id(id: Long?): LinkLocaleStatsDOBuilder = apply { this.id = id }

        open fun fullShortUrl(fullShortUrl: String?): LinkLocaleStatsDOBuilder = apply { this.fullShortUrl = fullShortUrl }

        open fun date(date: Date?): LinkLocaleStatsDOBuilder = apply { this.date = date }

        open fun cnt(cnt: Int?): LinkLocaleStatsDOBuilder = apply { this.cnt = cnt }

        open fun province(province: String?): LinkLocaleStatsDOBuilder = apply { this.province = province }

        open fun city(city: String?): LinkLocaleStatsDOBuilder = apply { this.city = city }

        open fun adcode(adcode: String?): LinkLocaleStatsDOBuilder = apply { this.adcode = adcode }

        open fun country(country: String?): LinkLocaleStatsDOBuilder = apply { this.country = country }

        open fun build(): LinkLocaleStatsDO = LinkLocaleStatsDO(
            id,
            fullShortUrl,
            date,
            cnt,
            province,
            city,
            adcode,
            country,
        )

        override fun toString(): String =
            "LinkLocaleStatsDO.LinkLocaleStatsDOBuilder(id=$id, fullShortUrl=$fullShortUrl, date=$date, " +
            "cnt=$cnt, province=$province, city=$city, " +
            "adcode=$adcode, country=$country)"
    }
}
