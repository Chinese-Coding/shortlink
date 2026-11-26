package dev.haotangyuan.shortlink.dao.entity

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName
import dev.haotangyuan.shortlink.common.database.BaseDO

import java.util.Date

/**
 * 浏览器统计访问实体
 *
 * @author haotangyuan
 */
@TableName("t_link_browser_stats")
open class LinkBrowserStatsDO(
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
     * 浏览器
     */
    open var browser: String? = null,
) : BaseDO() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinkBrowserStatsDO || !other.canEqual(this)) return false
        return id == other.id &&
            fullShortUrl == other.fullShortUrl &&
            date == other.date &&
            cnt == other.cnt &&
            browser == other.browser
    }

    override fun canEqual(other: Any?): Boolean = other is LinkBrowserStatsDO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (id?.hashCode() ?: 43)
        result = result * 59 + (fullShortUrl?.hashCode() ?: 43)
        result = result * 59 + (date?.hashCode() ?: 43)
        result = result * 59 + (cnt?.hashCode() ?: 43)
        result = result * 59 + (browser?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "LinkBrowserStatsDO(id=$id, fullShortUrl=$fullShortUrl, date=$date, cnt=$cnt, browser=$browser)"

    companion object {
        @JvmStatic
        fun builder(): LinkBrowserStatsDOBuilder = LinkBrowserStatsDOBuilder()
    }

    open class LinkBrowserStatsDOBuilder internal constructor() {
        private var id: Long? = null
        private var fullShortUrl: String? = null
        private var date: Date? = null
        private var cnt: Int? = null
        private var browser: String? = null

        open fun id(id: Long?): LinkBrowserStatsDOBuilder = apply { this.id = id }

        open fun fullShortUrl(fullShortUrl: String?): LinkBrowserStatsDOBuilder = apply { this.fullShortUrl = fullShortUrl }

        open fun date(date: Date?): LinkBrowserStatsDOBuilder = apply { this.date = date }

        open fun cnt(cnt: Int?): LinkBrowserStatsDOBuilder = apply { this.cnt = cnt }

        open fun browser(browser: String?): LinkBrowserStatsDOBuilder = apply { this.browser = browser }

        open fun build(): LinkBrowserStatsDO = LinkBrowserStatsDO(id, fullShortUrl, date, cnt, browser)

        override fun toString(): String =
            "LinkBrowserStatsDO.LinkBrowserStatsDOBuilder(id=$id, fullShortUrl=$fullShortUrl, date=$date, " +
            "cnt=$cnt, browser=$browser)"
    }
}
