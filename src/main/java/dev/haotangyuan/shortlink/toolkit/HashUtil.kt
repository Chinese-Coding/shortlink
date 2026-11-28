package dev.haotangyuan.shortlink.toolkit

import cn.hutool.crypto.digest.BCrypt

/**
 * HASH 工具类
 *
 * @author haotangyuan
 */
open class HashUtil {
    companion object {
        private const val ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"

        /** BCrypt 密码加密 */
        @JvmStatic
        fun encryptByBcrypt(rawPassword: String?): String = BCrypt.hashpw(rawPassword)

        /** BCrypt 密码校验 */
        @JvmStatic
        fun decryptByBcrypt(rawPassword: String?, encodedPassword: String?): Boolean =
            BCrypt.checkpw(rawPassword, encodedPassword)
    }
}
