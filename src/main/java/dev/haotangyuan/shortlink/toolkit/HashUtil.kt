package dev.haotangyuan.shortlink.toolkit

import cn.hutool.core.lang.hash.MurmurHash
import cn.hutool.crypto.digest.BCrypt

/**
 * HASH 工具类
 *
 * @author haotangyuan
 */
open class HashUtil {
    companion object {
        private const val ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"

        private fun convertDecToBase62(number: Long): String = buildString {
            var remaining = number
            while (remaining > 0) {
                append(ALPHABET[(remaining % ALPHABET.length).toInt()])
                remaining /= ALPHABET.length
            }
            reverse()
        }

        @JvmStatic
        fun hashToBase62(str: String?): String {
            val hash = MurmurHash.hash32(str)
            val number = if (hash < 0) Int.MAX_VALUE - hash.toLong() else hash.toLong()
            return convertDecToBase62(number)
        }

        /** BCrypt 密码加密 */
        @JvmStatic
        fun encryptByBcrypt(rawPassword: String?): String = BCrypt.hashpw(rawPassword)

        /** BCrypt 密码校验 */
        @JvmStatic
        fun decryptByBcrypt(rawPassword: String?, encodedPassword: String?): Boolean =
            BCrypt.checkpw(rawPassword, encodedPassword)
    }
}
