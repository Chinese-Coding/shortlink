package dev.haotangyuan.shortlink.vo

/**
 * 短链接浏览器监控响应参数
 *
 * @author haotangyuan
 */
open class LinkStatsBrowserVO(
    /**
     * 统计
     */
    open var cnt: Int? = null,

    /**
     * 浏览器
     */
    open var browser: String? = null,

    /**
     * 占比
     */
    open var ratio: Double? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinkStatsBrowserVO || !other.canEqual(this)) return false
        return cnt == other.cnt &&
            browser == other.browser &&
            java.util.Objects.equals(ratio, other.ratio)
    }

    protected open fun canEqual(other: Any?): Boolean = other is LinkStatsBrowserVO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (cnt?.hashCode() ?: 43)
        result = result * 59 + (browser?.hashCode() ?: 43)
        result = result * 59 + (ratio?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "LinkStatsBrowserVO(cnt=$cnt, browser=$browser, ratio=$ratio)"

    companion object {
        @JvmStatic
        fun builder(): LinkStatsBrowserVOBuilder = LinkStatsBrowserVOBuilder()
    }

    open class LinkStatsBrowserVOBuilder internal constructor() {
        private var cnt: Int? = null
        private var browser: String? = null
        private var ratio: Double? = null

        open fun cnt(cnt: Int?): LinkStatsBrowserVOBuilder = apply { this.cnt = cnt }

        open fun browser(browser: String?): LinkStatsBrowserVOBuilder = apply { this.browser = browser }

        open fun ratio(ratio: Double?): LinkStatsBrowserVOBuilder = apply { this.ratio = ratio }

        open fun build(): LinkStatsBrowserVO = LinkStatsBrowserVO(cnt, browser, ratio)

        override fun toString(): String =
            "LinkStatsBrowserVO.LinkStatsBrowserVOBuilder(cnt=$cnt, browser=$browser, ratio=$ratio)"
    }
}
