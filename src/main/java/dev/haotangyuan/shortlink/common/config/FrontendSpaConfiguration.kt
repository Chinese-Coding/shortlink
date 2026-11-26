package dev.haotangyuan.shortlink.common.config

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.IOException

@Configuration
open class FrontendSpaConfiguration {
    @Bean
    open fun frontendSpaForwardFilter(): FilterRegistrationBean<Filter> =
        FilterRegistrationBean<Filter>().apply {
            setFilter(FrontendSpaForwardFilter())
            addUrlPatterns("/app", "/app/*")
        }

    internal open class FrontendSpaForwardFilter : Filter {
        @Throws(IOException::class, ServletException::class)
        override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
            val httpRequest = request as HttpServletRequest
            val path = httpRequest.requestURI.substring(httpRequest.contextPath.length)
            if (isSpaRoute(path)) {
                request.getRequestDispatcher("/app/index.html").forward(request, response)
                return
            }
            chain.doFilter(request, response)
        }

        private fun isSpaRoute(path: String): Boolean =
            path == "/app" || path == "/app/" ||
                (path.startsWith("/app/") && '.' !in path.substringAfterLast('/'))
    }
}
