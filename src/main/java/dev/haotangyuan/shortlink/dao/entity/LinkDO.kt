package dev.haotangyuan.shortlink.dao.entity

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName
import dev.haotangyuan.shortlink.common.database.BaseDO

import java.util.Date

/**
 * 短链接实体
 *
 * @author haotangyuan
 */
@TableName("t_link")
open class LinkDO(
    /**
     * id
     */
    open var id: Long? = null,

    /**
     * 域名
     */
    open var domain: String? = null,

    /**
     * 短链接
     */
    open var shortUri: String? = null,

    /**
     * 完整短链接
     */
    open var fullShortUrl: String? = null,

    /**
     * 原始链接
     */
    open var originUrl: String? = null,

    /**
     * 点击量
     */
    open var clickNum: Int? = null,

    /**
     * 分组标识
     */
    open var gid: String? = null,

    /**
     * 启用标识 0：启用 1：未启用
     */
    open var enableStatus: Int? = null,

    /**
     * 创建类型 0：接口创建 1：控制台创建
     */
    open var createdType: Int? = null,

    /**
     * 有效期类型 0：永久有效 1：自定义
     */
    open var validDateType: Int? = null,

    /**
     * 有效期
     */
    open var validDate: Date? = null,

    /**
     * 描述
     */
    @field:TableField("\"describe\"")
    open var describe: String? = null,

    /**
     * 网站标识
     */
    open var favicon: String? = null,

    /**
     * 历史PV
     */
    open var totalPv: Int? = null,

    /**
     * 历史UV
     */
    open var totalUv: Int? = null,

    /**
     * 历史UIP
     */
    open var totalUip: Int? = null,

    /**
     * 今日PV
     */
    @field:TableField(exist = false)
    open var todayPv: Int? = null,

    /**
     * 今日UV
     */
    @field:TableField(exist = false)
    open var todayUv: Int? = null,

    /**
     * 今日UIP
     */
    @field:TableField(exist = false)
    open var todayUip: Int? = null,

    /**
     * 删除时间
     */
    open var delTime: Long? = null,
) : BaseDO() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinkDO || !other.canEqual(this)) return false
        return id == other.id &&
            domain == other.domain &&
            shortUri == other.shortUri &&
            fullShortUrl == other.fullShortUrl &&
            originUrl == other.originUrl &&
            clickNum == other.clickNum &&
            gid == other.gid &&
            enableStatus == other.enableStatus &&
            createdType == other.createdType &&
            validDateType == other.validDateType &&
            validDate == other.validDate &&
            describe == other.describe &&
            favicon == other.favicon &&
            totalPv == other.totalPv &&
            totalUv == other.totalUv &&
            totalUip == other.totalUip &&
            todayPv == other.todayPv &&
            todayUv == other.todayUv &&
            todayUip == other.todayUip &&
            delTime == other.delTime
    }

    override fun canEqual(other: Any?): Boolean = other is LinkDO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (id?.hashCode() ?: 43)
        result = result * 59 + (domain?.hashCode() ?: 43)
        result = result * 59 + (shortUri?.hashCode() ?: 43)
        result = result * 59 + (fullShortUrl?.hashCode() ?: 43)
        result = result * 59 + (originUrl?.hashCode() ?: 43)
        result = result * 59 + (clickNum?.hashCode() ?: 43)
        result = result * 59 + (gid?.hashCode() ?: 43)
        result = result * 59 + (enableStatus?.hashCode() ?: 43)
        result = result * 59 + (createdType?.hashCode() ?: 43)
        result = result * 59 + (validDateType?.hashCode() ?: 43)
        result = result * 59 + (validDate?.hashCode() ?: 43)
        result = result * 59 + (describe?.hashCode() ?: 43)
        result = result * 59 + (favicon?.hashCode() ?: 43)
        result = result * 59 + (totalPv?.hashCode() ?: 43)
        result = result * 59 + (totalUv?.hashCode() ?: 43)
        result = result * 59 + (totalUip?.hashCode() ?: 43)
        result = result * 59 + (todayPv?.hashCode() ?: 43)
        result = result * 59 + (todayUv?.hashCode() ?: 43)
        result = result * 59 + (todayUip?.hashCode() ?: 43)
        result = result * 59 + (delTime?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "LinkDO(id=$id, domain=$domain, shortUri=$shortUri, fullShortUrl=$fullShortUrl, originUrl=$originUrl, clickNum=$clickNum, gid=$gid, enableStatus=$enableStatus, createdType=$createdType, validDateType=$validDateType, " +
            "validDate=$validDate, describe=$describe, favicon=$favicon, totalPv=$totalPv, totalUv=$totalUv, totalUip=$totalUip, todayPv=$todayPv, todayUv=$todayUv, todayUip=$todayUip, delTime=$delTime)"

    companion object {
        @JvmStatic
        fun builder(): LinkDOBuilder = LinkDOBuilder()
    }

    open class LinkDOBuilder internal constructor() {
        private var id: Long? = null
        private var domain: String? = null
        private var shortUri: String? = null
        private var fullShortUrl: String? = null
        private var originUrl: String? = null
        private var clickNum: Int? = null
        private var gid: String? = null
        private var enableStatus: Int? = null
        private var createdType: Int? = null
        private var validDateType: Int? = null
        private var validDate: Date? = null
        private var describe: String? = null
        private var favicon: String? = null
        private var totalPv: Int? = null
        private var totalUv: Int? = null
        private var totalUip: Int? = null
        private var todayPv: Int? = null
        private var todayUv: Int? = null
        private var todayUip: Int? = null
        private var delTime: Long? = null

        open fun id(id: Long?): LinkDOBuilder = apply { this.id = id }

        open fun domain(domain: String?): LinkDOBuilder = apply { this.domain = domain }

        open fun shortUri(shortUri: String?): LinkDOBuilder = apply { this.shortUri = shortUri }

        open fun fullShortUrl(fullShortUrl: String?): LinkDOBuilder = apply { this.fullShortUrl = fullShortUrl }

        open fun originUrl(originUrl: String?): LinkDOBuilder = apply { this.originUrl = originUrl }

        open fun clickNum(clickNum: Int?): LinkDOBuilder = apply { this.clickNum = clickNum }

        open fun gid(gid: String?): LinkDOBuilder = apply { this.gid = gid }

        open fun enableStatus(enableStatus: Int?): LinkDOBuilder = apply { this.enableStatus = enableStatus }

        open fun createdType(createdType: Int?): LinkDOBuilder = apply { this.createdType = createdType }

        open fun validDateType(validDateType: Int?): LinkDOBuilder = apply { this.validDateType = validDateType }

        open fun validDate(validDate: Date?): LinkDOBuilder = apply { this.validDate = validDate }

        open fun describe(describe: String?): LinkDOBuilder = apply { this.describe = describe }

        open fun favicon(favicon: String?): LinkDOBuilder = apply { this.favicon = favicon }

        open fun totalPv(totalPv: Int?): LinkDOBuilder = apply { this.totalPv = totalPv }

        open fun totalUv(totalUv: Int?): LinkDOBuilder = apply { this.totalUv = totalUv }

        open fun totalUip(totalUip: Int?): LinkDOBuilder = apply { this.totalUip = totalUip }

        open fun todayPv(todayPv: Int?): LinkDOBuilder = apply { this.todayPv = todayPv }

        open fun todayUv(todayUv: Int?): LinkDOBuilder = apply { this.todayUv = todayUv }

        open fun todayUip(todayUip: Int?): LinkDOBuilder = apply { this.todayUip = todayUip }

        open fun delTime(delTime: Long?): LinkDOBuilder = apply { this.delTime = delTime }

        open fun build(): LinkDO = LinkDO(
            id,
            domain,
            shortUri,
            fullShortUrl,
            originUrl,
            clickNum,
            gid,
            enableStatus,
            createdType,
            validDateType,
            validDate,
            describe,
            favicon,
            totalPv,
            totalUv,
            totalUip,
            todayPv,
            todayUv,
            todayUip,
            delTime,
        )

        override fun toString(): String =
            "LinkDO.LinkDOBuilder(id=$id, domain=$domain, shortUri=$shortUri, " +
            "fullShortUrl=$fullShortUrl, originUrl=$originUrl, clickNum=$clickNum, " +
            "gid=$gid, enableStatus=$enableStatus, createdType=$createdType, " +
            "validDateType=$validDateType, validDate=$validDate, describe=$describe, " +
            "favicon=$favicon, totalPv=$totalPv, totalUv=$totalUv, " +
            "totalUip=$totalUip, todayPv=$todayPv, todayUv=$todayUv, " +
            "todayUip=$todayUip, delTime=$delTime)"
    }
}
