package dev.haotangyuan.shortlink.toolkit

import cn.hutool.core.date.DateUnit
import cn.hutool.core.date.DateUtil
import cn.hutool.core.util.StrUtil
import cn.hutool.core.util.URLUtil
import com.google.common.net.InternetDomainName
import dev.haotangyuan.shortlink.common.config.GotoDomainWhiteListConfiguration
import dev.haotangyuan.shortlink.common.constant.LinkConstant.Companion.DEFAULT_CACHE_VALID_TIME
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component
import java.net.IDN
import java.net.InetAddress
import java.net.URL
import java.util.Date
import java.util.Locale

/**
 * 短链接工具类
 *
 * @author: haotangyuan
 */
@Component
open class LinkUtil(
    private val whiteListCfg: GotoDomainWhiteListConfiguration,
) {

    /**
     * 获取站点 favicon
     */
    open fun getFavicon(url: String?): String? {
        if (StrUtil.isBlank(url)) return null
        var u = url!!.trim()
        if (!u.matches(SCHEME_PATTERN)) {
            u = "http://$u"
        }
        val parsed: URL = try {
            URLUtil.url(u)
        } catch (ex: Exception) {
            return null
        }
        val scheme = parsed.protocol
        val host = parsed.host
        val port = parsed.port
        if (StrUtil.isBlank(host)
            || !("http".equals(scheme, ignoreCase = true) || "https".equals(scheme, ignoreCase = true))
        ) {
            return null
        }

        val domain = extractDomain(u) ?: return null
        // 白名单：使用硬编码映射，避免网络抓取
        if (whiteListCfg.enable == true
            && whiteListCfg.details?.any { it.equals(domain, ignoreCase = true) } == true
        ) {
            return WhitelistFavicons.get(domain) ?: "https://$domain/favicon.ico"
        }

        val baseUrl = StringBuilder()
            .append(scheme).append("://").append(host)
        if (port > 0 && port != parsed.defaultPort) {
            baseUrl.append(":").append(port)
        }
        return baseUrl.append("/favicon.ico").toString()
    }

    /**
     * 白名单 favicon 硬编码映射
     */
    private object WhitelistFavicons {
        private val MAP: Map<String, String> = hashMapOf(
            "chanler.dev" to "https://chanler.dev/favicon.ico",
            "zhihu.com" to "https://www.zhihu.com/favicon.ico",
            "juejin.cn" to "https://lf-web-assets.juejin.cn/obj/juejin-web/xitu_juejin_web/static/favicons/favicon-32x32.png",
            "cnblogs.com" to "https://www.cnblogs.com/favicon.ico",
            "bilibili.com" to "https://www.bilibili.com/favicon.ico",
            "github.com" to "https://github.com/favicon.ico",
            "csdn.net" to "https://www.csdn.net/favicon.ico",
            "weixin.qq.com" to "https://res.wx.qq.com/a/wx_fed/assets/res/NTI4MWU5.ico",
            "qq.com" to "https://www.qq.com/favicon.ico",
            "toutiao.com" to "https://www.toutiao.com/favicon.ico",
            "weibo.com" to "https://weibo.com/favicon.ico",
            "douban.com" to "https://www.douban.com/favicon.ico",
            "jianshu.com" to "https://www.jianshu.com/favicon.ico",
        )

        fun get(domain: String): String? = MAP[domain.lowercase(Locale.ROOT)]
    }

    companion object {
        private val SCHEME_PATTERN = Regex("^[a-zA-Z][a-zA-Z0-9+.-]*://.*$")
        private val IPV4_PATTERN = Regex("^\\d+\\.\\d+\\.\\d+\\.\\d+$")

        /**
         * 获取短链接缓存有效时间
         *
         * @param validDate 有效期时间
         * @return 缓存有效时间，单位：ms
         */
        /**
         * 与 Java 的 String.isBlank 一致：只把 Character.isWhitespace 认定的字符视为空白。
         * Kotlin 的 isBlank 还会把不间断空格等字符当作空白。
         */
        private fun isJavaBlank(text: String): Boolean = text.codePoints().allMatch { Character.isWhitespace(it) }

        @JvmStatic
        fun getLinkCacheValidTime(validDate: Date?): Long =
            validDate?.let { DateUtil.between(Date(), it, DateUnit.MS) } ?: DEFAULT_CACHE_VALID_TIME

        /**
         * 获取实际访问IP
         *
         * @param request HttpServletResponse对象
         * @return 实际访问IP
         */
        @JvmStatic
        fun getActualIp(request: HttpServletRequest?): String? {
            if (request == null) {
                return null
            }
            val headerNames = arrayOf(
                "X-Forwarded-For",
                "X-Real-IP",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR",
            )
            for (header in headerNames) {
                val ip = request.getHeader(header)
                if (ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) {
                    continue
                }
                // 与 Java 的 String.split 一致：去掉末尾空串，值全是逗号时数组为空，取 [0] 会抛异常
                val first = ip.split(",").dropLastWhile { it.isEmpty() }.toTypedArray()[0].trim()
                if (first.isNotEmpty() && !"unknown".equals(first, ignoreCase = true)) {
                    return first
                }
            }
            return request.remoteAddr
        }

        /**
         * 获取操作系统
         *
         * @param request HttpServletResponse对象
         * @return 操作系统
         */
        @JvmStatic
        fun getOs(request: HttpServletRequest): String {
            val userAgent = request.getHeader("User-Agent")
            if (userAgent == null || isJavaBlank(userAgent)) {
                return "Unknown"
            }
            val ua = userAgent.lowercase(Locale.ROOT)
            return when {
                ua.contains("android") -> "Android"
                ua.contains("iphone") || ua.contains("ipad") || ua.contains("ipod") -> "iOS"
                ua.contains("windows") -> "Windows"
                ua.contains("mac") -> "Mac"
                ua.contains("x11") || ua.contains("linux") -> "Unix"
                else -> "Unknown"
            }
        }

        /**
         * 获取浏览器
         *
         * @param request HttpServletResponse对象
         * @return 浏览器
         */
        @JvmStatic
        fun getBrowser(request: HttpServletRequest): String {
            val userAgent = request.getHeader("User-Agent")
            if (userAgent == null || isJavaBlank(userAgent)) {
                return "Unknown"
            }
            val ua = userAgent.lowercase(Locale.ROOT)
            return when {
                ua.contains("edg") -> "Edge"
                ua.contains("msie") || ua.contains("trident") -> "Internet Explorer"
                ua.contains("opera") || ua.contains("opr") -> "Opera"
                ua.contains("chrome") -> "Chrome"
                ua.contains("safari") && !ua.contains("chrome") -> "Safari"
                ua.contains("firefox") -> "Firefox"
                else -> "Unknown"
            }
        }

        /**
         * 获取设备类型
         *
         * @param request HttpServletResponse对象
         * @return 设备类型
         */
        @JvmStatic
        fun getDevice(request: HttpServletRequest): String {
            val userAgent = request.getHeader("User-Agent")
            if (userAgent == null || isJavaBlank(userAgent)) {
                return "Unknown"
            }
            return if (userAgent.lowercase(Locale.ROOT).contains("mobile")) "Mobile" else "Desktop"
        }

        @JvmStatic
        fun extractDomain(url: String?): String? {
            if (StrUtil.isBlank(url)) return null
            var u = url!!.trim()
            if (!u.matches(SCHEME_PATTERN)) u = "http://$u" // 补scheme

            val parsed: URL = try {
                URLUtil.url(u)
            } catch (ex: Exception) {
                return null
            }
            var host: String? = parsed.host
            if (StrUtil.isBlank(host)) return null

            host = host!!.lowercase(Locale.ROOT)
            if (host.endsWith(".")) host = host.substring(0, host.length - 1)
            try {
                host = IDN.toASCII(host)
            } catch (ignore: Exception) {
            }

            // IP / localhost 直接忽略
            if ("localhost" == host || host.matches(IPV4_PATTERN) || host.contains(":")) return null

            try {
                val idn = InternetDomainName.from(host)
                if (idn.isUnderPublicSuffix || idn.isTopPrivateDomain) {
                    return idn.topPrivateDomain().toString() // eTLD+1
                }
            } catch (ignore: IllegalArgumentException) {
            }
            return null
        }

        /**
         * 判断 URL 是否为公网域名的 HTTP(S) 链接。
         */
        @JvmStatic
        fun isPublicHttpUrl(url: String?): Boolean {
            if (StrUtil.isBlank(url)) return false
            var normalized = url!!.trim()
            if (!normalized.matches(SCHEME_PATTERN)) {
                normalized = "http://$normalized"
            }
            return try {
                val parsed = URLUtil.url(normalized)
                val protocol = parsed.protocol
                ("http".equals(protocol, ignoreCase = true) || "https".equals(protocol, ignoreCase = true))
                    && extractDomain(normalized) != null
            } catch (ex: Exception) {
                false
            }
        }

        /**
         * 判断 URL 是否为解析到公网地址的 HTTP(S) 链接。
         */
        @JvmStatic
        fun isSafePublicHttpUrl(url: String?): Boolean {
            if (url == null || !isPublicHttpUrl(url)) return false
            var normalized = url.trim()
            if (!normalized.matches(SCHEME_PATTERN)) {
                normalized = "http://$normalized"
            }
            return try {
                val parsed = URLUtil.url(normalized)
                val addresses = InetAddress.getAllByName(parsed.host)
                if (addresses.isEmpty()) return false
                for (address in addresses) {
                    val bytes = address.address
                    val uniqueLocalIpv6 = bytes.size == 16 && (bytes[0].toInt() and 0xfe) == 0xfc
                    if (address.isAnyLocalAddress
                        || address.isLoopbackAddress
                        || address.isLinkLocalAddress
                        || address.isSiteLocalAddress
                        || address.isMulticastAddress
                        || uniqueLocalIpv6
                    ) {
                        return false
                    }
                }
                true
            } catch (ex: Exception) {
                false
            }
        }
    }
}
