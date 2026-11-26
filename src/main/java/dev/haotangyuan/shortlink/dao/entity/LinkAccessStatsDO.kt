package dev.haotangyuan.shortlink.dao.entity

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName

import java.util.Date

/**
 * 短链接基础访问(PV UV UIP)实体
 *
 * @author haotangyuan
 */
@TableName("t_link_access_stats")
open class LinkAccessStatsDO(
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
    open var pv: Int? = null,

    /**
     * 独立访客数
     */
    open var uv: Int? = null,

    /**
     * 独立ip数
     */
    open var uip: Int? = null,

    /**
     * 小时
     */
    @field:TableField("stats_hour")
    open var hour: Int? = null,

    /**
     * 星期
     */
    @field:TableField("stats_weekday")
    open var weekday: Int? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinkAccessStatsDO || !other.canEqual(this)) return false
        return id == other.id &&
            fullShortUrl == other.fullShortUrl &&
            date == other.date &&
            pv == other.pv &&
            uv == other.uv &&
            uip == other.uip &&
            hour == other.hour &&
            weekday == other.weekday
    }

    protected open fun canEqual(other: Any?): Boolean = other is LinkAccessStatsDO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (id?.hashCode() ?: 43)
        result = result * 59 + (fullShortUrl?.hashCode() ?: 43)
        result = result * 59 + (date?.hashCode() ?: 43)
        result = result * 59 + (pv?.hashCode() ?: 43)
        result = result * 59 + (uv?.hashCode() ?: 43)
        result = result * 59 + (uip?.hashCode() ?: 43)
        result = result * 59 + (hour?.hashCode() ?: 43)
        result = result * 59 + (weekday?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "LinkAccessStatsDO(id=$id, fullShortUrl=$fullShortUrl, date=$date, pv=$pv, " +
            "uv=$uv, uip=$uip, hour=$hour, weekday=$weekday)"

    companion object {
        @JvmStatic
        fun builder(): LinkAccessStatsDOBuilder = LinkAccessStatsDOBuilder()
    }

    open class LinkAccessStatsDOBuilder internal constructor() {
        private var id: Long? = null
        private var fullShortUrl: String? = null
        private var date: Date? = null
        private var pv: Int? = null
        private var uv: Int? = null
        private var uip: Int? = null
        private var hour: Int? = null
        private var weekday: Int? = null

        open fun id(id: Long?): LinkAccessStatsDOBuilder = apply { this.id = id }

        open fun fullShortUrl(fullShortUrl: String?): LinkAccessStatsDOBuilder = apply { this.fullShortUrl = fullShortUrl }

        open fun date(date: Date?): LinkAccessStatsDOBuilder = apply { this.date = date }

        open fun pv(pv: Int?): LinkAccessStatsDOBuilder = apply { this.pv = pv }

        open fun uv(uv: Int?): LinkAccessStatsDOBuilder = apply { this.uv = uv }

        open fun uip(uip: Int?): LinkAccessStatsDOBuilder = apply { this.uip = uip }

        open fun hour(hour: Int?): LinkAccessStatsDOBuilder = apply { this.hour = hour }

        open fun weekday(weekday: Int?): LinkAccessStatsDOBuilder = apply { this.weekday = weekday }

        open fun build(): LinkAccessStatsDO = LinkAccessStatsDO(id, fullShortUrl, date, pv, uv, uip, hour, weekday)

        override fun toString(): String =
            "LinkAccessStatsDO.LinkAccessStatsDOBuilder(id=$id, fullShortUrl=$fullShortUrl, date=$date, " +
            "pv=$pv, uv=$uv, uip=$uip, " +
            "hour=$hour, weekday=$weekday)"
    }
}
