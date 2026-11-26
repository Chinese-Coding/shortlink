package dev.haotangyuan.shortlink.vo

/**
 * 短链接创建响应参数
 *
 * @author haotangyuan
 */
open class LinkCreateVO(
    /**
     * 分组标识
     */
    open var gid: String? = null,

    /**
     * 原始链接
     */
    open var originUrl: String? = null,

    /**
     * 短链接
     */
    open var fullShortUrl: String? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinkCreateVO || !other.canEqual(this)) return false
        return gid == other.gid &&
            originUrl == other.originUrl &&
            fullShortUrl == other.fullShortUrl
    }

    protected open fun canEqual(other: Any?): Boolean = other is LinkCreateVO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (gid?.hashCode() ?: 43)
        result = result * 59 + (originUrl?.hashCode() ?: 43)
        result = result * 59 + (fullShortUrl?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "LinkCreateVO(gid=$gid, originUrl=$originUrl, fullShortUrl=$fullShortUrl)"

    companion object {
        @JvmStatic
        fun builder(): LinkCreateVOBuilder = LinkCreateVOBuilder()
    }

    open class LinkCreateVOBuilder internal constructor() {
        private var gid: String? = null
        private var originUrl: String? = null
        private var fullShortUrl: String? = null

        open fun gid(gid: String?): LinkCreateVOBuilder = apply { this.gid = gid }

        open fun originUrl(originUrl: String?): LinkCreateVOBuilder = apply { this.originUrl = originUrl }

        open fun fullShortUrl(fullShortUrl: String?): LinkCreateVOBuilder = apply { this.fullShortUrl = fullShortUrl }

        open fun build(): LinkCreateVO = LinkCreateVO(gid, originUrl, fullShortUrl)

        override fun toString(): String =
            "LinkCreateVO.LinkCreateVOBuilder(gid=$gid, originUrl=$originUrl, fullShortUrl=$fullShortUrl)"
    }
}
