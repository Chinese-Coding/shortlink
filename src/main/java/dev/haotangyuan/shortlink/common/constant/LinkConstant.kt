package dev.haotangyuan.shortlink.common.constant

/**
 * 短链接常量类
 *
 * @author haotangyuan
 */
open class LinkConstant {
    companion object {
        /**
         * 永久短链接缓存有效时间，单位：ms
         * 默认值为 30 天
         */
        const val DEFAULT_CACHE_VALID_TIME: Long = 2626560000L

        /**
         * UV Cookie 最大有效期（秒）- 3个月
         */
        const val VISITOR_COOKIE_MAX_AGE_SECONDS: Int = 60 * 60 * 24 * 90
    }
}
