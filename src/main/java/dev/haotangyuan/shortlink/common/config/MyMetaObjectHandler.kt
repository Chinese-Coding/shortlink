package dev.haotangyuan.shortlink.common.config

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
import org.apache.ibatis.reflection.MetaObject
import org.springframework.stereotype.Component
import java.util.Date
import java.util.function.Supplier

/**
 * @author haotangyuan
 */
@Component
class MyMetaObjectHandler : MetaObjectHandler {
    override fun insertFill(metaObject: MetaObject) {
        strictInsertFill(metaObject, "createTime", Supplier { Date() }, Date::class.java)
        strictInsertFill(metaObject, "updateTime", Supplier { Date() }, Date::class.java)
        strictInsertFill(metaObject, "delFlag", Supplier { 0 }, Int::class.javaObjectType)
    }

    override fun updateFill(metaObject: MetaObject) {
        strictInsertFill(metaObject, "updateTime", Supplier { Date() }, Date::class.java)
    }
}
