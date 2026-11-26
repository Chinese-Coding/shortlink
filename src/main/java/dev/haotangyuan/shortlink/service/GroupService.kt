package dev.haotangyuan.shortlink.service

import dev.haotangyuan.shortlink.dto.req.GroupSortReqDTO
import dev.haotangyuan.shortlink.dto.req.GroupUpdateReqDTO
import dev.haotangyuan.shortlink.vo.GroupVO

/**
 * 短链接分组接口层
 *
 * @author haotangyuan
 */
interface GroupService {
    /**
     * 新增短链接分组
     *
     * @param groupName 短链接分组名
     * @return void
     */
    fun saveGroup(groupName: String?)

    /**
     * 新增短链接分组
     *
     * @param username  用户名
     * @param groupName 短链接分组名
     * @return void
     */
    fun saveGroup(username: String, groupName: String?)

    /**
     * 查询短链接分组集合
     *
     * @return List<GroupVO>
     */
    fun listGroup(): List<GroupVO>

    /**
     * 修改短链接分组
     *
     * @param groupUpdateReqDTO 短链接分组参数
     */
    fun updateGroup(groupUpdateReqDTO: GroupUpdateReqDTO)

    /**
     * 删除短链接分组
     *
     * @param gid 短链接分组标识
     */
    fun deleteGroup(gid: String)

    /**
     * 短链接分组排序
     *
     * @param groupSortReqDTOs
     */
    fun sortGroup(groupSortReqDTOs: MutableList<GroupSortReqDTO>)
}
