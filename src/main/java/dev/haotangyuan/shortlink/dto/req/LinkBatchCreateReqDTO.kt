package dev.haotangyuan.shortlink.dto.req

import com.fasterxml.jackson.annotation.JsonFormat

import java.util.Date

/**
 * 短链接批量创建请求对象
 *
 * @author haotangyuan
 */
open class LinkBatchCreateReqDTO {

    /**
     * 原始链接集合
     */
    open var originUrls: MutableList<String>? = null

    /**
     * 描述集合
     */
    open var describes: MutableList<String>? = null

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinkBatchCreateReqDTO || !other.canEqual(this)) return false
        return originUrls == other.originUrls &&
            describes == other.describes &&
            gid == other.gid &&
            createdType == other.createdType &&
            validDateType == other.validDateType &&
            validDate == other.validDate
    }

    protected open fun canEqual(other: Any?): Boolean = other is LinkBatchCreateReqDTO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (originUrls?.hashCode() ?: 43)
        result = result * 59 + (describes?.hashCode() ?: 43)
        result = result * 59 + (gid?.hashCode() ?: 43)
        result = result * 59 + (createdType?.hashCode() ?: 43)
        result = result * 59 + (validDateType?.hashCode() ?: 43)
        result = result * 59 + (validDate?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "LinkBatchCreateReqDTO(originUrls=$originUrls, describes=$describes, gid=$gid, " +
            "createdType=$createdType, validDateType=$validDateType, validDate=$validDate)"
}
