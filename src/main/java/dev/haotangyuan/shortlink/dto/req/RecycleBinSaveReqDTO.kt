package dev.haotangyuan.shortlink.dto.req

/**
 * 回收站保存请求参数
 *
 * @author haotangyuan
 */
open class RecycleBinSaveReqDTO {

    /**
     * 分组标识
     */
    open var gid: String? = null

    /**
     * 短链接
     */
    open var fullShortUrl: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RecycleBinSaveReqDTO || !other.canEqual(this)) return false
        return gid == other.gid &&
            fullShortUrl == other.fullShortUrl
    }

    protected open fun canEqual(other: Any?): Boolean = other is RecycleBinSaveReqDTO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (gid?.hashCode() ?: 43)
        result = result * 59 + (fullShortUrl?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "RecycleBinSaveReqDTO(gid=$gid, fullShortUrl=$fullShortUrl)"
}
