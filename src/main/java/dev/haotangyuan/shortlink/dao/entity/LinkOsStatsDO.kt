package dev.haotangyuan.shortlink.dao.entity

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName
import dev.haotangyuan.shortlink.common.database.BaseDO

import java.util.Date

/**
 * 操作系统访问统计实体
 *
 * @author haotangyuan
 */
@TableName("t_link_os_stats")
open class LinkOsStatsDO(
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
     * 操作系统
     */
    open var os: String? = null,
) : BaseDO() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinkOsStatsDO || !other.canEqual(this)) return false
        return id == other.id &&
            fullShortUrl == other.fullShortUrl &&
            date == other.date &&
            cnt == other.cnt &&
            os == other.os
    }

    override fun canEqual(other: Any?): Boolean = other is LinkOsStatsDO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (id?.hashCode() ?: 43)
        result = result * 59 + (fullShortUrl?.hashCode() ?: 43)
        result = result * 59 + (date?.hashCode() ?: 43)
        result = result * 59 + (cnt?.hashCode() ?: 43)
        result = result * 59 + (os?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "LinkOsStatsDO(id=$id, fullShortUrl=$fullShortUrl, date=$date, cnt=$cnt, os=$os)"

    companion object {
        @JvmStatic
        fun builder(): LinkOsStatsDOBuilder = LinkOsStatsDOBuilder()
    }

    open class LinkOsStatsDOBuilder internal constructor() {
        private var id: Long? = null
        private var fullShortUrl: String? = null
        private var date: Date? = null
        private var cnt: Int? = null
        private var os: String? = null

        open fun id(id: Long?): LinkOsStatsDOBuilder = apply { this.id = id }

        open fun fullShortUrl(fullShortUrl: String?): LinkOsStatsDOBuilder = apply { this.fullShortUrl = fullShortUrl }

        open fun date(date: Date?): LinkOsStatsDOBuilder = apply { this.date = date }

        open fun cnt(cnt: Int?): LinkOsStatsDOBuilder = apply { this.cnt = cnt }

        open fun os(os: String?): LinkOsStatsDOBuilder = apply { this.os = os }

        open fun build(): LinkOsStatsDO = LinkOsStatsDO(id, fullShortUrl, date, cnt, os)

        override fun toString(): String =
            "LinkOsStatsDO.LinkOsStatsDOBuilder(id=$id, fullShortUrl=$fullShortUrl, date=$date, " +
            "cnt=$cnt, os=$os)"
    }
}
