package dev.haotangyuan.shortlink.dto.req

import com.fasterxml.jackson.annotation.JsonFormat

import java.util.Date

/**
 * 短链接创建请求参数
 *
 * @author haotangyuan
 */
open class LinkCreateReqDTO {

    /**
     * 域名
     */
    open var domain: String? = null

    /**
     * 原始链接
     */
    open var originUrl: String? = null

    /**
     * 分组标识
     */
    open var gid: String? = null

    /**
     * 创建类型 0：接口创建 1：控制台创建
     */
    open var createdType: Int? = null

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
     * 描述
     */
    open var describe: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinkCreateReqDTO || !other.canEqual(this)) return false
        return domain == other.domain &&
            originUrl == other.originUrl &&
            gid == other.gid &&
            createdType == other.createdType &&
            validDateType == other.validDateType &&
            validDate == other.validDate &&
            describe == other.describe
    }

    protected open fun canEqual(other: Any?): Boolean = other is LinkCreateReqDTO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (domain?.hashCode() ?: 43)
        result = result * 59 + (originUrl?.hashCode() ?: 43)
        result = result * 59 + (gid?.hashCode() ?: 43)
        result = result * 59 + (createdType?.hashCode() ?: 43)
        result = result * 59 + (validDateType?.hashCode() ?: 43)
        result = result * 59 + (validDate?.hashCode() ?: 43)
        result = result * 59 + (describe?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "LinkCreateReqDTO(domain=$domain, originUrl=$originUrl, gid=$gid, createdType=$createdType, " +
            "validDateType=$validDateType, validDate=$validDate, describe=$describe)"
}
