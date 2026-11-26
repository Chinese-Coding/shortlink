package dev.haotangyuan.shortlink.vo

import com.fasterxml.jackson.annotation.JsonFormat

import java.util.Date

/**
 * 短链接监控访问记录响应参数
 *
 * @author haotangyuan
 */
open class LinkStatsAccessRecordVO(
    /**
     * 访客类型
     */
    open var uvType: String? = null,

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
     * 用户信息
     */
    open var user: String? = null,

    /**
     * 访问时间
     */
    @field:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    open var createTime: Date? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinkStatsAccessRecordVO || !other.canEqual(this)) return false
        return uvType == other.uvType &&
            browser == other.browser &&
            os == other.os &&
            ip == other.ip &&
            network == other.network &&
            device == other.device &&
            locale == other.locale &&
            user == other.user &&
            createTime == other.createTime
    }

    protected open fun canEqual(other: Any?): Boolean = other is LinkStatsAccessRecordVO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (uvType?.hashCode() ?: 43)
        result = result * 59 + (browser?.hashCode() ?: 43)
        result = result * 59 + (os?.hashCode() ?: 43)
        result = result * 59 + (ip?.hashCode() ?: 43)
        result = result * 59 + (network?.hashCode() ?: 43)
        result = result * 59 + (device?.hashCode() ?: 43)
        result = result * 59 + (locale?.hashCode() ?: 43)
        result = result * 59 + (user?.hashCode() ?: 43)
        result = result * 59 + (createTime?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "LinkStatsAccessRecordVO(uvType=$uvType, browser=$browser, os=$os, ip=$ip, network=$network, " +
            "device=$device, locale=$locale, user=$user, createTime=$createTime)"

    companion object {
        @JvmStatic
        fun builder(): LinkStatsAccessRecordVOBuilder = LinkStatsAccessRecordVOBuilder()
    }

    open class LinkStatsAccessRecordVOBuilder internal constructor() {
        private var uvType: String? = null
        private var browser: String? = null
        private var os: String? = null
        private var ip: String? = null
        private var network: String? = null
        private var device: String? = null
        private var locale: String? = null
        private var user: String? = null
        private var createTime: Date? = null

        open fun uvType(uvType: String?): LinkStatsAccessRecordVOBuilder = apply { this.uvType = uvType }

        open fun browser(browser: String?): LinkStatsAccessRecordVOBuilder = apply { this.browser = browser }

        open fun os(os: String?): LinkStatsAccessRecordVOBuilder = apply { this.os = os }

        open fun ip(ip: String?): LinkStatsAccessRecordVOBuilder = apply { this.ip = ip }

        open fun network(network: String?): LinkStatsAccessRecordVOBuilder = apply { this.network = network }

        open fun device(device: String?): LinkStatsAccessRecordVOBuilder = apply { this.device = device }

        open fun locale(locale: String?): LinkStatsAccessRecordVOBuilder = apply { this.locale = locale }

        open fun user(user: String?): LinkStatsAccessRecordVOBuilder = apply { this.user = user }

        open fun createTime(createTime: Date?): LinkStatsAccessRecordVOBuilder = apply { this.createTime = createTime }

        open fun build(): LinkStatsAccessRecordVO = LinkStatsAccessRecordVO(
            uvType,
            browser,
            os,
            ip,
            network,
            device,
            locale,
            user,
            createTime,
        )

        override fun toString(): String =
            "LinkStatsAccessRecordVO.LinkStatsAccessRecordVOBuilder(uvType=$uvType, browser=$browser, os=$os, " +
            "ip=$ip, network=$network, device=$device, " +
            "locale=$locale, user=$user, createTime=$createTime)"
    }
}
