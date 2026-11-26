package dev.haotangyuan.shortlink.common.database

import com.baomidou.mybatisplus.annotation.FieldFill
import com.baomidou.mybatisplus.annotation.TableField

import java.util.Date

/**
 * 数据库持久层实体基础属性
 *
 * @author haotangyuan
 */
open class BaseDO {

    /**
     * 创建时间
     */
    @field:TableField(fill = FieldFill.INSERT)
    open var createTime: Date? = null

    /**
     * 修改时间
     */
    @field:TableField(fill = FieldFill.INSERT_UPDATE)
    open var updateTime: Date? = null

    /**
     * 删除标识 0：未删除 1：已删除
     */
    @field:TableField(fill = FieldFill.INSERT)
    open var delFlag: Int? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaseDO || !other.canEqual(this)) return false
        return createTime == other.createTime &&
            updateTime == other.updateTime &&
            delFlag == other.delFlag
    }

    protected open fun canEqual(other: Any?): Boolean = other is BaseDO

    override fun hashCode(): Int {
        var result = 1
        result = result * 59 + (createTime?.hashCode() ?: 43)
        result = result * 59 + (updateTime?.hashCode() ?: 43)
        result = result * 59 + (delFlag?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String =
        "BaseDO(createTime=$createTime, updateTime=$updateTime, delFlag=$delFlag)"
}
