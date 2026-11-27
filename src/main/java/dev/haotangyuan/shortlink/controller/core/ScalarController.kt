package dev.haotangyuan.shortlink.controller.core

import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import java.io.IOException

@Controller
open class ScalarController {
    @GetMapping("/scalar.html")
    @Throws(IOException::class)
    open fun scalar(response: HttpServletResponse) {
        response.contentType = MediaType.TEXT_HTML_VALUE
        ClassPathResource("static/scalar.html").inputStream.use { input ->
            response.outputStream.write(input.readAllBytes())
        }
    }
}
