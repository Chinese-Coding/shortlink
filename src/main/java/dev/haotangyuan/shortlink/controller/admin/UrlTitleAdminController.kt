package dev.haotangyuan.shortlink.controller.admin

import dev.haotangyuan.shortlink.common.convention.result.Result
import dev.haotangyuan.shortlink.common.convention.result.Results
import dev.haotangyuan.shortlink.service.UrlTitleService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * URL 标题控制层
 *
 * @author haotangyuan
 */
@RestController
open class UrlTitleAdminController(private val urlTitleService: UrlTitleService) {

    /**
     * 根据 URL 获取标题
     *
     * @param url URL
     * @return Result<String>
     */
    @GetMapping("/api/short-link/admin/v1/title")
    open fun getTitleByUrl(@RequestParam("url") url: String): Result<String> =
        Results.success(urlTitleService.getTitleByUrl(url))
}
