package dev.haotangyuan.shortlink.vo

/**
 * 短链接基础信息响应参数
 *
 * @author haotangyuan
 */
open class LinkBaseInfoVO(
    /**
     * 描述信息
     */
    open var describe: String? = null,

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
        if (other !is LinkBaseInfoVO || !other.canEqual(this)) return false
        return describe == other.describe &&
            originUrl == other.originUrl &&
            fullShortUrl == other.fullShortUrl
    }

    protected open fun canEqual(other: Any?): Boolean = other is LinkBaseInfoVO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (describe?.hashCode() ?: 43)
        result = result * 59 + (originUrl?.hashCode() ?: 43)
        result = result * 59 + (fullShortUrl?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "LinkBaseInfoVO(describe=$describe, originUrl=$originUrl, fullShortUrl=$fullShortUrl)"

    companion object {
        @JvmStatic
        fun builder(): LinkBaseInfoVOBuilder = LinkBaseInfoVOBuilder()
    }

    open class LinkBaseInfoVOBuilder internal constructor() {
        private var describe: String? = null
        private var originUrl: String? = null
        private var fullShortUrl: String? = null

        open fun describe(describe: String?): LinkBaseInfoVOBuilder = apply { this.describe = describe }

        open fun originUrl(originUrl: String?): LinkBaseInfoVOBuilder = apply { this.originUrl = originUrl }

        open fun fullShortUrl(fullShortUrl: String?): LinkBaseInfoVOBuilder = apply { this.fullShortUrl = fullShortUrl }

        open fun build(): LinkBaseInfoVO = LinkBaseInfoVO(describe, originUrl, fullShortUrl)

        override fun toString(): String =
            "LinkBaseInfoVO.LinkBaseInfoVOBuilder(describe=$describe, originUrl=$originUrl, fullShortUrl=$fullShortUrl)"
    }
}
