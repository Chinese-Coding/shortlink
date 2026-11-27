package dev.haotangyuan.shortlink.dao.entity

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName
import dev.haotangyuan.shortlink.common.database.BaseDO

import java.util.Date

/**
 * 访问设备统计访问实体
 *
 * @author haotangyuan
 */
@TableName("t_link_device_stats")
open class LinkDeviceStatsDO(
    /**
     * id
     */
    open var id: Long? = null,

    /**
     * 完整短链接
     */
    open var fullShortUrl: String? = null,

    /**
     * 日期
     */
    @field:TableField("stats_date")
    open var date: Date? = null,

    /**
     * 访问量
     */
    open var cnt: Int? = null,

    /**
     * 访问设备
     */
    open var device: String? = null,
) : BaseDO() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LinkDeviceStatsDO || !other.canEqual(this)) return false
        return id == other.id &&
            fullShortUrl == other.fullShortUrl &&
            date == other.date &&
            cnt == other.cnt &&
            device == other.device
    }

    override fun canEqual(other: Any?): Boolean = other is LinkDeviceStatsDO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (id?.hashCode() ?: 43)
        result = result * 59 + (fullShortUrl?.hashCode() ?: 43)
        result = result * 59 + (date?.hashCode() ?: 43)
        result = result * 59 + (cnt?.hashCode() ?: 43)
        result = result * 59 + (device?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "LinkDeviceStatsDO(id=$id, fullShortUrl=$fullShortUrl, date=$date, cnt=$cnt, device=$device)"

    companion object {
        @JvmStatic
        fun builder(): LinkDeviceStatsDOBuilder = LinkDeviceStatsDOBuilder()
    }

    open class LinkDeviceStatsDOBuilder internal constructor() {
        private var id: Long? = null
        private var fullShortUrl: String? = null
        private var date: Date? = null
        private var cnt: Int? = null
        private var device: String? = null

        open fun id(id: Long?): LinkDeviceStatsDOBuilder = apply { this.id = id }

        open fun fullShortUrl(fullShortUrl: String?): LinkDeviceStatsDOBuilder = apply { this.fullShortUrl = fullShortUrl }

        open fun date(date: Date?): LinkDeviceStatsDOBuilder = apply { this.date = date }

        open fun cnt(cnt: Int?): LinkDeviceStatsDOBuilder = apply { this.cnt = cnt }

        open fun device(device: String?): LinkDeviceStatsDOBuilder = apply { this.device = device }

        open fun build(): LinkDeviceStatsDO = LinkDeviceStatsDO(id, fullShortUrl, date, cnt, device)

        override fun toString(): String =
            "LinkDeviceStatsDO.LinkDeviceStatsDOBuilder(id=$id, fullShortUrl=$fullShortUrl, date=$date, " +
            "cnt=$cnt, device=$device)"
    }
}
