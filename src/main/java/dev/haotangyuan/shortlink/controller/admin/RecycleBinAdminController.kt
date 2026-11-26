package dev.haotangyuan.shortlink.controller.admin

import com.baomidou.mybatisplus.core.metadata.IPage
import dev.haotangyuan.shortlink.common.convention.result.Result
import dev.haotangyuan.shortlink.common.convention.result.Results
import dev.haotangyuan.shortlink.dto.req.RecycleBinLinkPageReqDTO
import dev.haotangyuan.shortlink.dto.req.RecycleBinRemoveReqDTO
import dev.haotangyuan.shortlink.dto.req.RecycleBinRestoreReqDTO
import dev.haotangyuan.shortlink.dto.req.RecycleBinSaveReqDTO
import dev.haotangyuan.shortlink.vo.LinkPageVO
import dev.haotangyuan.shortlink.service.RecycleBinService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * 回收站控制层
 *
 * @author haotangyuan
 */
@RestController
open class RecycleBinAdminController(private val recycleBinService: RecycleBinService) {

    /**
     * 保存回收站数据
     *
     * @param recycleBinSaveReqDTO 回收站保存请求参数
     * @return 结果
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/save")
    open fun saveRecycledBin(@RequestBody recycleBinSaveReqDTO: RecycleBinSaveReqDTO): Result<Void> {
        recycleBinService.saveRecycledBin(recycleBinSaveReqDTO)
        return Results.success()
    }

    /**
     * 回收站分页查询
     *
     * @param recycleBinLinkPageReqDTO 分页请求参数
     * @return Result<IPage<LinkPageVO>>
     */
    @GetMapping("/api/short-link/admin/v1/recycle-bin/page")
    open fun pageLink(recycleBinLinkPageReqDTO: RecycleBinLinkPageReqDTO): Result<IPage<LinkPageVO>> =
        Results.success(recycleBinService.pageRecycleBinLink(recycleBinLinkPageReqDTO))

    /**
     * 恢复短链接
     *
     * @param recycleBinRestoreReqDTO 恢复请求参数
     * @return Result<Void>
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/restore")
    open fun restoreLink(@RequestBody recycleBinRestoreReqDTO: RecycleBinRestoreReqDTO): Result<Void> {
        recycleBinService.restoreLink(recycleBinRestoreReqDTO)
        return Results.success()
    }

    /**
     * 从回收站移除短链接
     *
     * @param recycleBinRemoveReqDTO 回收站移除请求参数
     * @return Result<Void>
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/remove")
    open fun removeLink(@RequestBody recycleBinRemoveReqDTO: RecycleBinRemoveReqDTO): Result<Void> {
        recycleBinService.removeLink(recycleBinRemoveReqDTO)
        return Results.success()
    }
}
