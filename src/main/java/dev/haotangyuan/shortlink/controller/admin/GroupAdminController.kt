package dev.haotangyuan.shortlink.controller.admin

import dev.haotangyuan.shortlink.common.convention.result.Result
import dev.haotangyuan.shortlink.common.convention.result.Results
import dev.haotangyuan.shortlink.dto.req.GroupSaveReqDTO
import dev.haotangyuan.shortlink.dto.req.GroupSortReqDTO
import dev.haotangyuan.shortlink.dto.req.GroupUpdateReqDTO
import dev.haotangyuan.shortlink.vo.GroupVO
import dev.haotangyuan.shortlink.service.GroupService
import org.springframework.web.bind.annotation.*

/**
 * 短链接分组控制层
 *
 * @author haotangyuan
 */
@RestController
class GroupAdminController(private val groupService: GroupService) {

    /**
     * 创建短链接分组分组
     *
     * @param groupSaveReqDTO
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/group")
    fun create(@RequestBody groupSaveReqDTO: GroupSaveReqDTO): Result<Void> {
        groupService.saveGroup(groupSaveReqDTO.name)
        return Results.success()
    }

    /**
     * 查询短链接分组列表
     *
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/group")
    fun listGroup(): Result<List<GroupVO>> =
        Results.success(groupService.listGroup())

    /**
     * 修改短链接分组名称
     *
     * @param groupUpdateReqDTO
     * @return
     */
    @PutMapping("/api/short-link/admin/v1/group")
    fun updateGroup(@RequestBody groupUpdateReqDTO: GroupUpdateReqDTO): Result<Void> {
        groupService.updateGroup(groupUpdateReqDTO)
        return Results.success()
    }

    /**
     * 删除短链接分组名称
     *
     * @param gid
     * @return
     */
    @DeleteMapping("/api/short-link/admin/v1/group")
    fun deleteGroup(@RequestParam gid: String): Result<Void> {
        groupService.deleteGroup(gid)
        return Results.success()
    }

    /**
     * 删除短链接分组名称
     *
     * @param groupSortReqDTOs
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/group/sort")
    fun sortGroup(@RequestBody groupSortReqDTOs: MutableList<GroupSortReqDTO>): Result<Void> {
        groupService.sortGroup(groupSortReqDTOs)
        return Results.success()
    }
}
