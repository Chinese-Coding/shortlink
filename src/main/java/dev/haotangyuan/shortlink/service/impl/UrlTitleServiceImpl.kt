package dev.haotangyuan.shortlink.service.impl

import dev.haotangyuan.shortlink.common.convention.exception.ClientException
import dev.haotangyuan.shortlink.service.UrlTitleService
import dev.haotangyuan.shortlink.toolkit.LinkUtil
import org.springframework.stereotype.Service
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern

/** 简化版 URL 标题获取 */
@Service
open class UrlTitleServiceImpl : UrlTitleService {
    @Suppress("DEPRECATION")
    override fun getTitleByUrl(url: String?): String {
        if (url == null || url.codePoints().allMatch(Character::isWhitespace)) {
            throw ClientException("链接不能为空")
        }
        if (!LinkUtil.isSafePublicHttpUrl(url)) {
            throw ClientException("仅支持公网 HTTP/HTTPS 链接")
        }

        var connection: HttpURLConnection? = null
        try {
            connection = URL(url).openConnection() as HttpURLConnection
            connection.connectTimeout = 4000
            connection.readTimeout = 4000
            connection.instanceFollowRedirects = false
            connection.setRequestProperty("User-Agent", "Mozilla/5.0")
            if (connection.responseCode !in 200..299) {
                return ""
            }
            return connection.inputStream.bufferedReader(Charsets.UTF_8).use { reader ->
                val content = buildString {
                    var character = reader.read()
                    while (character != -1 && length < MAX_BYTES) {
                        append(character.toChar())
                        character = reader.read()
                    }
                }
                val match = TITLE.matcher(content)
                if (match.find()) {
                    WHITESPACE.matcher(match.group(1)).replaceAll(" ").trim { it <= ' ' }.take(128)
                } else {
                    ""
                }
            }
        } catch (_: Exception) {
            return ""
        } finally {
            connection?.disconnect()
        }
    }

    companion object {
        private val TITLE = Pattern.compile("(?is)<title[^>]*>(.*?)</title>")
        private val WHITESPACE = Pattern.compile("\\s+")
        private const val MAX_BYTES = 8 * 1024
    }
}
