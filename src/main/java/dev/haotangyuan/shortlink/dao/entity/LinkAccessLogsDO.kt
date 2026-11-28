package dev.haotangyuan.shortlink.dao.entity

import com.baomidou.mybatisplus.annotation.TableName
import dev.haotangyuan.shortlink.common.database.BaseDO

/**
 * 短链接访问日志实体
 *
 * @author haotangyuan
 */
@TableName("t_link_access_logs")
open class LinkAccessLogsDO(
    /**
     * id
     */
    open var id: Long? = null,

    /**
     * 完整短链接
     */
    open var fullShortUrl: String? = null,

    /**
     * 访客标识
     */
    open var visitorId: String? = null,

    /**
     * 浏览器
     */
    open var browser: String? = null,

    /**
     * 操作系统
     */
    open var os: String? = null,

    /**
     * ip
     */
    open var ip: String? = null,

    /**
     * 访问网络
     */
    open var network: String? = null,

    /**
     * 访问设备
     */
    open var device: String? = null,

    /**
     * 地区
     */
    open var locale: String? = null,

    /**
     * 首访标记
     */
    open var firstFlag: Boolean? = null,

    /**
     * 消息ID（Redis Stream RecordId，用于 DB 层幂等兜底）
     */
    open var messageId: String? = null,
) : BaseDO() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinkAccessLogsDO || !other.canEqual(this)) return false
        if (!super.equals(other)) return false
        return id == other.id &&
            fullShortUrl == other.fullShortUrl &&
            visitorId == other.visitorId &&
            browser == other.browser &&
            os == other.os &&
            ip == other.ip &&
            network == other.network &&
            device == other.device &&
            locale == other.locale &&
            firstFlag == other.firstFlag &&
            messageId == other.messageId
    }

    override fun canEqual(other: Any?): Boolean = other is LinkAccessLogsDO

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = result * 59 + (id?.hashCode() ?: 43)
        result = result * 59 + (fullShortUrl?.hashCode() ?: 43)
        result = result * 59 + (visitorId?.hashCode() ?: 43)
        result = result * 59 + (browser?.hashCode() ?: 43)
        result = result * 59 + (os?.hashCode() ?: 43)
        result = result * 59 + (ip?.hashCode() ?: 43)
        result = result * 59 + (network?.hashCode() ?: 43)
        result = result * 59 + (device?.hashCode() ?: 43)
        result = result * 59 + (locale?.hashCode() ?: 43)
        result = result * 59 + (firstFlag?.hashCode() ?: 43)
        result = result * 59 + (messageId?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "LinkAccessLogsDO(id=$id, fullShortUrl=$fullShortUrl, visitorId=$visitorId, browser=$browser, os=$os, ip=$ip, " +
            "network=$network, device=$device, locale=$locale, firstFlag=$firstFlag, messageId=$messageId)"

    companion object {
        @JvmStatic
        fun builder(): LinkAccessLogsDOBuilder = LinkAccessLogsDOBuilder()
    }

    open class LinkAccessLogsDOBuilder internal constructor() {
        private var id: Long? = null
        private var fullShortUrl: String? = null
        private var visitorId: String? = null
        private var browser: String? = null
        private var os: String? = null
        private var ip: String? = null
        private var network: String? = null
        private var device: String? = null
        private var locale: String? = null
        private var firstFlag: Boolean? = null
        private var messageId: String? = null

        open fun id(id: Long?): LinkAccessLogsDOBuilder = apply { this.id = id }

        open fun fullShortUrl(fullShortUrl: String?): LinkAccessLogsDOBuilder = apply { this.fullShortUrl = fullShortUrl }

        open fun visitorId(visitorId: String?): LinkAccessLogsDOBuilder = apply { this.visitorId = visitorId }

        open fun browser(browser: String?): LinkAccessLogsDOBuilder = apply { this.browser = browser }

        open fun os(os: String?): LinkAccessLogsDOBuilder = apply { this.os = os }

        open fun ip(ip: String?): LinkAccessLogsDOBuilder = apply { this.ip = ip }

        open fun network(network: String?): LinkAccessLogsDOBuilder = apply { this.network = network }

        open fun device(device: String?): LinkAccessLogsDOBuilder = apply { this.device = device }

        open fun locale(locale: String?): LinkAccessLogsDOBuilder = apply { this.locale = locale }

        open fun firstFlag(firstFlag: Boolean?): LinkAccessLogsDOBuilder = apply { this.firstFlag = firstFlag }

        open fun messageId(messageId: String?): LinkAccessLogsDOBuilder = apply { this.messageId = messageId }

        open fun build(): LinkAccessLogsDO = LinkAccessLogsDO(
            id,
            fullShortUrl,
            visitorId,
            browser,
            os,
            ip,
            network,
            device,
            locale,
            firstFlag,
            messageId,
        )

        override fun toString(): String =
            "LinkAccessLogsDO.LinkAccessLogsDOBuilder(id=$id, fullShortUrl=$fullShortUrl, visitorId=$visitorId, " +
            "browser=$browser, os=$os, ip=$ip, " +
            "network=$network, device=$device, locale=$locale, " +
            "firstFlag=$firstFlag, messageId=$messageId)"
    }
}
