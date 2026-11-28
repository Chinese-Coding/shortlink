package dev.haotangyuan.shortlink.dto.biz

import java.util.Date

/**
 * 短链接统计实体
 *
 * @author haotangyuan
 */
open class LinkStatsRecordDTO(
    /**
     * 完整短链接
     */
    open var fullShortUrl: String? = null,

    /**
     * 访问用户IP
     */
    open var uip: String? = null,

    /**
     * 操作系统
     */
    open var os: String? = null,

    /**
     * 浏览器
     */
    open var browser: String? = null,

    /**
     * 操作设备
     */
    open var device: String? = null,

    /**
     * 访客标识，取自浏览器 visitorId Cookie 中的 UUID
     */
    open var visitorId: String? = null,

    /**
     * 访问时间
     */
    open var accessTime: Date? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinkStatsRecordDTO || !other.canEqual(this)) return false
        return fullShortUrl == other.fullShortUrl &&
            uip == other.uip &&
            os == other.os &&
            browser == other.browser &&
            device == other.device &&
            visitorId == other.visitorId &&
            accessTime == other.accessTime
    }

    protected open fun canEqual(other: Any?): Boolean = other is LinkStatsRecordDTO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (fullShortUrl?.hashCode() ?: 43)
        result = result * 59 + (uip?.hashCode() ?: 43)
        result = result * 59 + (os?.hashCode() ?: 43)
        result = result * 59 + (browser?.hashCode() ?: 43)
        result = result * 59 + (device?.hashCode() ?: 43)
        result = result * 59 + (visitorId?.hashCode() ?: 43)
        result = result * 59 + (accessTime?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "LinkStatsRecordDTO(fullShortUrl=$fullShortUrl, uip=$uip, os=$os, browser=$browser, " +
            "device=$device, visitorId=$visitorId, accessTime=$accessTime)"

    companion object {
        @JvmStatic
        fun builder(): LinkStatsRecordDTOBuilder = LinkStatsRecordDTOBuilder()
    }

    open class LinkStatsRecordDTOBuilder internal constructor() {
        private var fullShortUrl: String? = null
        private var uip: String? = null
        private var os: String? = null
        private var browser: String? = null
        private var device: String? = null
        private var visitorId: String? = null
        private var accessTime: Date? = null

        open fun fullShortUrl(fullShortUrl: String?): LinkStatsRecordDTOBuilder = apply { this.fullShortUrl = fullShortUrl }

        open fun uip(uip: String?): LinkStatsRecordDTOBuilder = apply { this.uip = uip }

        open fun os(os: String?): LinkStatsRecordDTOBuilder = apply { this.os = os }

        open fun browser(browser: String?): LinkStatsRecordDTOBuilder = apply { this.browser = browser }

        open fun device(device: String?): LinkStatsRecordDTOBuilder = apply { this.device = device }

        open fun visitorId(visitorId: String?): LinkStatsRecordDTOBuilder = apply { this.visitorId = visitorId }

        open fun accessTime(accessTime: Date?): LinkStatsRecordDTOBuilder = apply { this.accessTime = accessTime }

        open fun build(): LinkStatsRecordDTO = LinkStatsRecordDTO(
            fullShortUrl,
            uip,
            os,
            browser,
            device,
            visitorId,
            accessTime,
        )

        override fun toString(): String =
            "LinkStatsRecordDTO.LinkStatsRecordDTOBuilder(fullShortUrl=$fullShortUrl, uip=$uip, os=$os, " +
            "browser=$browser, device=$device, visitorId=$visitorId, " +
            "accessTime=$accessTime)"
    }
}
