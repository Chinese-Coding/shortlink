package dev.haotangyuan.shortlink.dao.entity

import com.baomidou.mybatisplus.annotation.TableName
import dev.haotangyuan.shortlink.common.database.BaseDO

/**
 * 分组实体
 *
 * @author haotangyuan
 */
@TableName("t_group")
open class GroupDO(
    /**
     * id
     */
    open var id: Long? = null,

    /**
     * 分组标识
     */
    open var gid: String? = null,

    /**
     * 分组名称
     */
    open var name: String? = null,

    /**
     * 创建分组用户名
     */
    open var username: String? = null,

    /**
     * 分组排序
     */
    open var sortOrder: Int? = null,
) : BaseDO() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GroupDO || !other.canEqual(this)) return false
        return id == other.id &&
            gid == other.gid &&
            name == other.name &&
            username == other.username &&
            sortOrder == other.sortOrder
    }

    override fun canEqual(other: Any?): Boolean = other is GroupDO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (id?.hashCode() ?: 43)
        result = result * 59 + (gid?.hashCode() ?: 43)
        result = result * 59 + (name?.hashCode() ?: 43)
        result = result * 59 + (username?.hashCode() ?: 43)
        result = result * 59 + (sortOrder?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "GroupDO(id=$id, gid=$gid, name=$name, username=$username, sortOrder=$sortOrder)"

    companion object {
        @JvmStatic
        fun builder(): GroupDOBuilder = GroupDOBuilder()
    }

    open class GroupDOBuilder internal constructor() {
        private var id: Long? = null
        private var gid: String? = null
        private var name: String? = null
        private var username: String? = null
        private var sortOrder: Int? = null

        open fun id(id: Long?): GroupDOBuilder = apply { this.id = id }

        open fun gid(gid: String?): GroupDOBuilder = apply { this.gid = gid }

        open fun name(name: String?): GroupDOBuilder = apply { this.name = name }

        open fun username(username: String?): GroupDOBuilder = apply { this.username = username }

        open fun sortOrder(sortOrder: Int?): GroupDOBuilder = apply { this.sortOrder = sortOrder }

        open fun build(): GroupDO = GroupDO(id, gid, name, username, sortOrder)

        override fun toString(): String =
            "GroupDO.GroupDOBuilder(id=$id, gid=$gid, name=$name, username=$username, sortOrder=$sortOrder)"
    }
}
