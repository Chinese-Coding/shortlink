package dev.haotangyuan.shortlink.common.enums

/**
 * 有效期类型
 *
 * @author haotangyuan
 */
enum class ValidDateTypeEnum(val type: Int) {
    /** 永久有效 */
    PERMANENT(0),

    /** 自定义有效期 */
    CUSTOM(1)
}
