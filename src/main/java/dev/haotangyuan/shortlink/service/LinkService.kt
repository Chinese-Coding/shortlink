package dev.haotangyuan.shortlink.service

import com.baomidou.mybatisplus.core.metadata.IPage
import dev.haotangyuan.shortlink.dto.biz.LinkStatsRecordDTO
import dev.haotangyuan.shortlink.dto.req.LinkBatchCreateReqDTO
import dev.haotangyuan.shortlink.dto.req.LinkCreateReqDTO
import dev.haotangyuan.shortlink.dto.req.LinkPageReqDTO
import dev.haotangyuan.shortlink.dto.req.LinkUpdateReqDTO
import dev.haotangyuan.shortlink.vo.GroupLinkCountQueryVO
import dev.haotangyuan.shortlink.vo.LinkBatchCreateVO
import dev.haotangyuan.shortlink.vo.LinkCreateVO
import dev.haotangyuan.shortlink.vo.LinkPageVO
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse

import java.io.IOException

/**
 * 短链接接口层
 *
 * @author haotangyuan
 */
interface LinkService {

    /**
     * 创建短链接
     *
     * @param linkCreateReqDTO
     * @return
     */
    fun createLink(linkCreateReqDTO: LinkCreateReqDTO): LinkCreateVO

    /**
     * 更新短链接
     *
     * @param linkUpdateReqDTO 短链接更新请求参数
     */
    fun updateLink(linkUpdateReqDTO: LinkUpdateReqDTO)

    /**
     * 短链接分页查询
     *
     * @param linkPageReqDTO 分页请求参数
     * @return IPage<LinkPageVO>
     */
    fun pageLink(linkPageReqDTO: LinkPageReqDTO): IPage<LinkPageVO>

    /**
     * 查询分组内短链接数量
     *
     * @param gidList 分组标识列表
     * @return List<GroupLinkCountQueryVO>
     */
    fun listGroupLinkCount(gidList: List<String>): List<GroupLinkCountQueryVO>

    /**
     * 根据短链接还原原始链接
     *
     * @param shortUri 短链接后缀
     * @param request  HttpServerRequest
     * @param response HttpServerResponse
     */
    @Throws(IOException::class)
    fun restoreUrl(shortUri: String, request: ServletRequest, response: ServletResponse)

    /**
     * 批量创建短链接
     *
     * @param linkBatchCreateReqDTO 短链接批量创建请求参数
     * @return LinkBatchCreateVO
     */
    fun batchCreateLink(linkBatchCreateReqDTO: LinkBatchCreateReqDTO): LinkBatchCreateVO

    /**
     * 短链接统计
     *
     * @param linkStatsRecordDTO 短链接统计实体参数
     */
    fun linkStats(linkStatsRecordDTO: LinkStatsRecordDTO)
}
