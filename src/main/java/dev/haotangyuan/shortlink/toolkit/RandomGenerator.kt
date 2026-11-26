package dev.haotangyuan.shortlink.toolkit

/**
 * @author haotangyuan
 */
class RandomGenerator {
    companion object {
        private const val CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"

        @JvmStatic
        fun generateRandom(): String = generateRandom(6)

        /**
         * 生成随机字符串
         *
         * @param length 长度
         * @return 随机字符串
         */
        @JvmStatic
        fun generateRandom(length: Int): String = buildString {
            for (index in 0 until length) {
                append(CHARACTERS[(Math.random() * CHARACTERS.length).toInt()])
            }
        }
    }
}
