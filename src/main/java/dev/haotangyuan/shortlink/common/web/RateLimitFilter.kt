package dev.haotangyuan.shortlink.common.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.util.concurrent.RateLimiter
import dev.haotangyuan.shortlink.common.config.RateLimitProperties
import dev.haotangyuan.shortlink.common.convention.result.Results
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

import java.io.IOException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

/**
 * 短链接系统限流过滤器
 *
 * @author haotangyuan
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
open class RateLimitFilter(
    @Qualifier("createRateLimiter") private val createRateLimiter: RateLimiter,
    @Qualifier("redirectRateLimiter") private val redirectRateLimiter: RateLimiter,
    @Qualifier("statsRateLimiter") private val statsRateLimiter: RateLimiter,
    private val props: RateLimitProperties,
) : OncePerRequestFilter() {

    // 异步和错误分派阶段不再执行。
    override fun shouldNotFilterAsyncDispatch(): Boolean = true

    override fun shouldNotFilterErrorDispatch(): Boolean = true

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(req: HttpServletRequest, resp: HttpServletResponse, filterChain: FilterChain) {
        if (!tryAcquire(req)) {
            tooMany(req, resp)
            return
        }
        filterChain.doFilter(req, resp)
    }

    private fun tryAcquire(req: HttpServletRequest): Boolean {
        if (isRedirectPath(req)) {
            return redirectRateLimiter.tryAcquire(1, (props.redirect ?: throw NullPointerException()).timeout.toLong(), TimeUnit.MILLISECONDS)
        }

        val path = req.requestURI
        val permitCount = creationPermitCount(path)
        return when {
            permitCount > 0 -> createRateLimiter.tryAcquire(
                permitCount,
                (props.create ?: throw NullPointerException()).timeout.toLong(),
                TimeUnit.MILLISECONDS,
            )
            path.startsWith("/api/short-link/admin/v1/stats") ->
                statsRateLimiter.tryAcquire(1, (props.stats ?: throw NullPointerException()).timeout.toLong(), TimeUnit.MILLISECONDS)
            else -> true
        }
    }

    private fun creationPermitCount(path: String?): Int = when (path) {
        "/api/short-link/v1/create", "/api/short-link/admin/v1/create" -> 1
        "/api/short-link/v1/create/batch", "/api/short-link/admin/v1/create/batch" -> 5
        else -> 0
    }

    private fun isRedirectPath(req: HttpServletRequest): Boolean =
        "GET".equals(req.method, ignoreCase = true) && SHORT_URI_PATH.matcher(req.requestURI).matches()

    private fun tooMany(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.status = 429
        if (isRedirectPath(req)) {
            resp.contentType = "text/html;charset=UTF-8"
            val query = req.queryString?.let { "?$it" }.orEmpty()
            val source = URLEncoder.encode(req.requestURI + query, StandardCharsets.UTF_8)
            resp.writer.write(String.format(TOO_MANY_HTML, source))
        } else {
            resp.contentType = "application/json;charset=UTF-8"
            val body = Results.failure("B100000", "当前流量较高，请稍后再试...")
            resp.writer.write(OBJECT_MAPPER.writeValueAsString(body))
        }
    }

    companion object {
        private val SHORT_URI_PATH = Pattern.compile("^/[A-Za-z0-9]{1,8}$")
        private val OBJECT_MAPPER = ObjectMapper()
        private val TOO_MANY_HTML = """
            <!doctype html><meta charset="utf-8">
            <title>Too Many Requests</title>
            <meta name="robots" content="noindex,nofollow">
            <style>body{font-family:system-ui;margin:6vh auto;max-width:600px;padding:0 16px;line-height:1.6}button{padding:.6em 1em;border:0;border-radius:10px;cursor:pointer}</style>
            <h1>访问太频繁</h1>
            <p>当前访问人数较多，请稍后再试。</p>
            <p>
              <button id="retry">立即重试</button>
              <button id="copy">复制短链</button>
            </p>
            <script>
              const src = decodeURIComponent('%s');
              document.getElementById('retry').onclick = () => location.href = src;
              document.getElementById('copy').onclick = async () => { try { await navigator.clipboard.writeText(location.origin + src); alert('已复制'); } catch(e){ alert('复制失败'); } }
            </script>
        """.trimIndent() + "\n"
    }
}
