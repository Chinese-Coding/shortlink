package dev.haotangyuan.shortlink.dto.req

import com.fasterxml.jackson.annotation.JsonFormat

import java.util.Date

/**
 * 短链接修改请求参数
 *
 * @author haotangyuan
 */
open class LinkUpdateReqDTO {

    /**
     * 原始链接
     */
    open var originUrl: String? = null

    /**
     * 完整短链接
     */
    open var fullShortUrl: String? = null

    /**
     * 原始分组标识
     */
    open var originGid: String? = null

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
     * 描述
     */
    open var describe: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinkUpdateReqDTO || !other.canEqual(this)) return false
        return originUrl == other.originUrl &&
            fullShortUrl == other.fullShortUrl &&
            originGid == other.originGid &&
            gid == other.gid &&
            validDateType == other.validDateType &&
            validDate == other.validDate &&
            describe == other.describe
    }

    protected open fun canEqual(other: Any?): Boolean = other is LinkUpdateReqDTO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (originUrl?.hashCode() ?: 43)
        result = result * 59 + (fullShortUrl?.hashCode() ?: 43)
        result = result * 59 + (originGid?.hashCode() ?: 43)
        result = result * 59 + (gid?.hashCode() ?: 43)
        result = result * 59 + (validDateType?.hashCode() ?: 43)
        result = result * 59 + (validDate?.hashCode() ?: 43)
        result = result * 59 + (describe?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "LinkUpdateReqDTO(originUrl=$originUrl, fullShortUrl=$fullShortUrl, originGid=$originGid, gid=$gid, " +
            "validDateType=$validDateType, validDate=$validDate, describe=$describe)"
}
