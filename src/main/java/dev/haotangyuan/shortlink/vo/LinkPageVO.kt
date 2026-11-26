package dev.haotangyuan.shortlink.vo

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer

import java.util.Date

/**
 * 短链接分页响应参数
 *
 * @author haotangyuan
 */
open class LinkPageVO {

    /**
     * id
     */
    @field:JsonSerialize(using = ToStringSerializer::class)
    open var id: Long? = null

    /**
     * 域名
     */
    open var domain: String? = null

    /**
     * 短链接
     */
    open var shortUri: String? = null

    /**
     * 完整短链接
     */
    open var fullShortUrl: String? = null

    /**
     * 原始链接
     */
    open var originUrl: String? = null

    /**
     * 分组标识
     */
    open var gid: String? = null

    /**
     * 有效期类型 0：永久有效 1：自定义
     */
    open var validDateType: Int? = null

    /**
     * 有效期
     */
    @field:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    open var validDate: Date? = null

    /**
     * 创建时间
     */
    @field:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    open var createTime: Date? = null

    /**
     * 描述
     */
    open var describe: String? = null

    /**
     * 网站标识
     */
    open var favicon: String? = null

    /**
     * 历史PV
     */
    open var totalPv: Int? = null

    /**
     * 历史UV
     */
    open var totalUv: Int? = null

    /**
     * 历史UIP
     */
    open var totalUip: Int? = null

    /**
     * 今日PV
     */
    open var todayPv: Int? = null

    /**
     * 今日UV
     */
    open var todayUv: Int? = null

    /**
     * 今日UIP
     */
    open var todayUip: Int? = null

    /**
     * 删除时间
     */
    open var delTime: Long? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinkPageVO || !other.canEqual(this)) return false
        return id == other.id &&
            domain == other.domain &&
            shortUri == other.shortUri &&
            fullShortUrl == other.fullShortUrl &&
            originUrl == other.originUrl &&
            gid == other.gid &&
            validDateType == other.validDateType &&
            validDate == other.validDate &&
            createTime == other.createTime &&
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

    protected open fun canEqual(other: Any?): Boolean = other is LinkPageVO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (id?.hashCode() ?: 43)
        result = result * 59 + (domain?.hashCode() ?: 43)
        result = result * 59 + (shortUri?.hashCode() ?: 43)
        result = result * 59 + (fullShortUrl?.hashCode() ?: 43)
        result = result * 59 + (originUrl?.hashCode() ?: 43)
        result = result * 59 + (gid?.hashCode() ?: 43)
        result = result * 59 + (validDateType?.hashCode() ?: 43)
        result = result * 59 + (validDate?.hashCode() ?: 43)
        result = result * 59 + (createTime?.hashCode() ?: 43)
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
        "LinkPageVO(id=$id, domain=$domain, shortUri=$shortUri, fullShortUrl=$fullShortUrl, originUrl=$originUrl, gid=$gid, validDateType=$validDateType, validDate=$validDate, createTime=$createTime, " +
            "describe=$describe, favicon=$favicon, totalPv=$totalPv, totalUv=$totalUv, totalUip=$totalUip, todayPv=$todayPv, todayUv=$todayUv, todayUip=$todayUip, delTime=$delTime)"
}
