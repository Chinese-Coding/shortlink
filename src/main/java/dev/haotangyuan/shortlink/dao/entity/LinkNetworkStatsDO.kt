package dev.haotangyuan.shortlink.dao.entity

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName
import dev.haotangyuan.shortlink.common.database.BaseDO

import java.util.Date

/**
 * 访问网络统计访问实体
 *
 * @author haotangyuan
 */
@TableName("t_link_network_stats")
open class LinkNetworkStatsDO(
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
     * 访问网络
     */
    open var network: String? = null,
) : BaseDO() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinkNetworkStatsDO || !other.canEqual(this)) return false
        return id == other.id &&
            fullShortUrl == other.fullShortUrl &&
            date == other.date &&
            cnt == other.cnt &&
            network == other.network
    }

    override fun canEqual(other: Any?): Boolean = other is LinkNetworkStatsDO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (id?.hashCode() ?: 43)
        result = result * 59 + (fullShortUrl?.hashCode() ?: 43)
        result = result * 59 + (date?.hashCode() ?: 43)
        result = result * 59 + (cnt?.hashCode() ?: 43)
        result = result * 59 + (network?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "LinkNetworkStatsDO(id=$id, fullShortUrl=$fullShortUrl, date=$date, cnt=$cnt, network=$network)"

    companion object {
        @JvmStatic
        fun builder(): LinkNetworkStatsDOBuilder = LinkNetworkStatsDOBuilder()
    }

    open class LinkNetworkStatsDOBuilder internal constructor() {
        private var id: Long? = null
        private var fullShortUrl: String? = null
        private var date: Date? = null
        private var cnt: Int? = null
        private var network: String? = null

        open fun id(id: Long?): LinkNetworkStatsDOBuilder = apply { this.id = id }

        open fun fullShortUrl(fullShortUrl: String?): LinkNetworkStatsDOBuilder = apply { this.fullShortUrl = fullShortUrl }

        open fun date(date: Date?): LinkNetworkStatsDOBuilder = apply { this.date = date }

        open fun cnt(cnt: Int?): LinkNetworkStatsDOBuilder = apply { this.cnt = cnt }

        open fun network(network: String?): LinkNetworkStatsDOBuilder = apply { this.network = network }

        open fun build(): LinkNetworkStatsDO = LinkNetworkStatsDO(id, fullShortUrl, date, cnt, network)

        override fun toString(): String =
            "LinkNetworkStatsDO.LinkNetworkStatsDOBuilder(id=$id, fullShortUrl=$fullShortUrl, date=$date, " +
            "cnt=$cnt, network=$network)"
    }
}
