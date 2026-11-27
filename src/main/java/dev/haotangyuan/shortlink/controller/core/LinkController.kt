package dev.haotangyuan.shortlink.controller.core

import com.baomidou.mybatisplus.core.metadata.IPage
import dev.haotangyuan.shortlink.common.convention.result.Result
import dev.haotangyuan.shortlink.common.convention.result.Results
import dev.haotangyuan.shortlink.dto.req.LinkBatchCreateReqDTO
import dev.haotangyuan.shortlink.dto.req.LinkCreateReqDTO
import dev.haotangyuan.shortlink.dto.req.LinkPageReqDTO
import dev.haotangyuan.shortlink.dto.req.LinkUpdateReqDTO
import dev.haotangyuan.shortlink.vo.GroupLinkCountQueryVO
import dev.haotangyuan.shortlink.vo.LinkBatchCreateVO
import dev.haotangyuan.shortlink.vo.LinkCreateVO
import dev.haotangyuan.shortlink.vo.LinkPageVO
import dev.haotangyuan.shortlink.service.LinkService
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import org.springframework.web.bind.annotation.*

import java.io.IOException

/**
 * 短链接控制层
 *
 * @author haotangyuan
 */
@RestController
open class LinkController(private val linkService: LinkService) {

    @GetMapping("/{shortUri}")
    @Throws(IOException::class)
    open fun restoreUrl(@PathVariable shortUri: String, request: ServletRequest, response: ServletResponse) {
        linkService.restoreUrl(shortUri, request, response)
    }

    /**
     * 创建短链接
     *
     * @return Result
     */
    @PostMapping("/api/short-link/v1/create")
    open fun createLink(@RequestBody linkCreateReqDTO: LinkCreateReqDTO): Result<LinkCreateVO> =
        Results.success(linkService.createLink(linkCreateReqDTO))

    /**
     * 批量创建短链接
     */
    @PostMapping("/api/short-link/v1/create/batch")
    open fun batchCreateShortLink(
        @RequestBody linkBatchCreateReqDTO: LinkBatchCreateReqDTO,
    ): Result<LinkBatchCreateVO> =
        Results.success(linkService.batchCreateLink(linkBatchCreateReqDTO))

    /**
     * 修改短链接
     *
     * @param linkUpdateReqDTO 短链接更新请求参数
     * @return Result<Void>
     */
    @PostMapping("/api/short-link/v1/update")
    open fun updateLink(@RequestBody linkUpdateReqDTO: LinkUpdateReqDTO): Result<Void> {
        linkService.updateLink(linkUpdateReqDTO)
        return Results.success()
    }

    /**
     * 短链接分页查询
     *
     * @param linkPageReqDTO 分页请求参数
     * @return Result<IPage<LinkPageVO>>
     */
    @GetMapping("/api/short-link/v1/page")
    open fun pageLink(linkPageReqDTO: LinkPageReqDTO): Result<IPage<LinkPageVO>> =
        Results.success(linkService.pageLink(linkPageReqDTO))

    /**
     * 查询分组内短链接数量
     *
     * @return Result<List<GroupLinkCountQueryVO>>
     */
    @GetMapping("/api/short-link/v1/count")
    open fun listGroupLinkCount(
        @RequestParam("gidList") gidList: List<String>,
    ): Result<List<GroupLinkCountQueryVO>> =
        Results.success(linkService.listGroupLinkCount(gidList))
}
