package dev.haotangyuan.shortlink.dao.entity

import com.baomidou.mybatisplus.annotation.TableName

import java.util.Date

/**
 * 首次访问判重表实体
 *
 * @author haotangyuan
 */
@TableName("t_link_first_visit")
open class LinkFirstVisitDO(
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
     * 首次访问时间
     */
    open var createTime: Date? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinkFirstVisitDO || !other.canEqual(this)) return false
        return id == other.id &&
            fullShortUrl == other.fullShortUrl &&
            visitorId == other.visitorId &&
            createTime == other.createTime
    }

    protected open fun canEqual(other: Any?): Boolean = other is LinkFirstVisitDO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (id?.hashCode() ?: 43)
        result = result * 59 + (fullShortUrl?.hashCode() ?: 43)
        result = result * 59 + (visitorId?.hashCode() ?: 43)
        result = result * 59 + (createTime?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "LinkFirstVisitDO(id=$id, fullShortUrl=$fullShortUrl, visitorId=$visitorId, createTime=$createTime)"

    companion object {
        @JvmStatic
        fun builder(): LinkFirstVisitDOBuilder = LinkFirstVisitDOBuilder()
    }

    open class LinkFirstVisitDOBuilder internal constructor() {
        private var id: Long? = null
        private var fullShortUrl: String? = null
        private var visitorId: String? = null
        private var createTime: Date? = null

        open fun id(id: Long?): LinkFirstVisitDOBuilder = apply { this.id = id }

        open fun fullShortUrl(fullShortUrl: String?): LinkFirstVisitDOBuilder = apply { this.fullShortUrl = fullShortUrl }

        open fun visitorId(visitorId: String?): LinkFirstVisitDOBuilder = apply { this.visitorId = visitorId }

        open fun createTime(createTime: Date?): LinkFirstVisitDOBuilder = apply { this.createTime = createTime }

        open fun build(): LinkFirstVisitDO = LinkFirstVisitDO(id, fullShortUrl, visitorId, createTime)

        override fun toString(): String =
            "LinkFirstVisitDO.LinkFirstVisitDOBuilder(id=$id, fullShortUrl=$fullShortUrl, visitorId=$visitorId, " +
            "createTime=$createTime)"
    }
}
