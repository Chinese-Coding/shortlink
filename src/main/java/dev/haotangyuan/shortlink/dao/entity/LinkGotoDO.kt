package dev.haotangyuan.shortlink.dao.entity

import com.baomidou.mybatisplus.annotation.TableName

/**
 * 短链接跳转实体
 *
 * @author haotangyuan
 */
@TableName("t_link_goto")
open class LinkGotoDO(
    /**
     * ID
     */
    open var id: Long? = null,

    /**
     * 分组标识
     */
    open var gid: String? = null,

    /**
     * 完整短链接
     */
    open var fullShortUrl: String? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinkGotoDO || !other.canEqual(this)) return false
        return id == other.id &&
            gid == other.gid &&
            fullShortUrl == other.fullShortUrl
    }

    protected open fun canEqual(other: Any?): Boolean = other is LinkGotoDO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (id?.hashCode() ?: 43)
        result = result * 59 + (gid?.hashCode() ?: 43)
        result = result * 59 + (fullShortUrl?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "LinkGotoDO(id=$id, gid=$gid, fullShortUrl=$fullShortUrl)"

    companion object {
        @JvmStatic
        fun builder(): LinkGotoDOBuilder = LinkGotoDOBuilder()
    }

    open class LinkGotoDOBuilder internal constructor() {
        private var id: Long? = null
        private var gid: String? = null
        private var fullShortUrl: String? = null

        open fun id(id: Long?): LinkGotoDOBuilder = apply { this.id = id }

        open fun gid(gid: String?): LinkGotoDOBuilder = apply { this.gid = gid }

        open fun fullShortUrl(fullShortUrl: String?): LinkGotoDOBuilder = apply { this.fullShortUrl = fullShortUrl }

        open fun build(): LinkGotoDO = LinkGotoDO(id, gid, fullShortUrl)

        override fun toString(): String =
            "LinkGotoDO.LinkGotoDOBuilder(id=$id, gid=$gid, fullShortUrl=$fullShortUrl)"
    }
}
