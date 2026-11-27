package dev.haotangyuan.shortlink.vo

/**
 * 短链接批量创建响应对象
 *
 * @author haotangyuan
 */
open class LinkBatchCreateVO(
    /**
     * 成功数量
     */
    open var total: Int? = null,

    /**
     * 批量创建返回参数
     */
    open var baseLinkInfos: MutableList<LinkBaseInfoVO>? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinkBatchCreateVO || !other.canEqual(this)) return false
        return total == other.total &&
            baseLinkInfos == other.baseLinkInfos
    }

    protected open fun canEqual(other: Any?): Boolean = other is LinkBatchCreateVO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (total?.hashCode() ?: 43)
        result = result * 59 + (baseLinkInfos?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "LinkBatchCreateVO(total=$total, baseLinkInfos=$baseLinkInfos)"

    companion object {
        @JvmStatic
        fun builder(): LinkBatchCreateVOBuilder = LinkBatchCreateVOBuilder()
    }

    open class LinkBatchCreateVOBuilder internal constructor() {
        private var total: Int? = null
        private var baseLinkInfos: MutableList<LinkBaseInfoVO>? = null

        open fun total(total: Int?): LinkBatchCreateVOBuilder = apply { this.total = total }

        open fun baseLinkInfos(baseLinkInfos: MutableList<LinkBaseInfoVO>?): LinkBatchCreateVOBuilder = apply { this.baseLinkInfos = baseLinkInfos }

        open fun build(): LinkBatchCreateVO = LinkBatchCreateVO(total, baseLinkInfos)

        override fun toString(): String =
            "LinkBatchCreateVO.LinkBatchCreateVOBuilder(total=$total, baseLinkInfos=$baseLinkInfos)"
    }
}
