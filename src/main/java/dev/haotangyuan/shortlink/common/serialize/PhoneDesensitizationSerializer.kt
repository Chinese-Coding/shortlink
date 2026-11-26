package dev.haotangyuan.shortlink.common.serialize

import cn.hutool.core.util.DesensitizedUtil
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.io.IOException

/**
 * 手机号脱敏序列化
 *
 * @author haotangyuan
 */
open class PhoneDesensitizationSerializer : JsonSerializer<String>() {
    @Throws(IOException::class)
    override fun serialize(phone: String?, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider) {
        jsonGenerator.writeString(DesensitizedUtil.mobilePhone(phone))
    }
}
