package dev.haotangyuan.shortlink.service

import com.baomidou.mybatisplus.core.metadata.IPage
import dev.haotangyuan.shortlink.dto.req.RecycleBinLinkPageReqDTO
import dev.haotangyuan.shortlink.dto.req.RecycleBinRemoveReqDTO
import dev.haotangyuan.shortlink.dto.req.RecycleBinRestoreReqDTO
import dev.haotangyuan.shortlink.dto.req.RecycleBinSaveReqDTO
import dev.haotangyuan.shortlink.vo.LinkPageVO

/**
 * 回收站接口层
 *
 * @author haotangyuan
 */
interface RecycleBinService {
    /**
     * 保存回收站数据
     *
     * @param recycleBinSaveReqDTO 回收站保存请求参数
     */
    fun saveRecycledBin(recycleBinSaveReqDTO: RecycleBinSaveReqDTO)

    /**
     * 短链接分页查询
     *
     * @param recycleBinLinkPageReqDTO 分页请求参数
     * @return IPage<LinkPageVO>
     */
    fun pageRecycleBinLink(recycleBinLinkPageReqDTO: RecycleBinLinkPageReqDTO): IPage<LinkPageVO>

    /**
     * 恢复短链接
     *
     * @param recycleBinRestoreReqDTO 恢复请求参数
     */
    fun restoreLink(recycleBinRestoreReqDTO: RecycleBinRestoreReqDTO)

    /**
     * 从回收站移除短链接
     *
     * @param recycleBinRemoveReqDTO 回收站移除请求参数
     */
    fun removeLink(recycleBinRemoveReqDTO: RecycleBinRemoveReqDTO)
}
