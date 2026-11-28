package dev.haotangyuan.shortlink.controller.core

import com.baomidou.mybatisplus.core.metadata.IPage
import dev.haotangyuan.shortlink.common.convention.result.Result
import dev.haotangyuan.shortlink.common.convention.result.Results
import dev.haotangyuan.shortlink.dto.req.GroupStatsAccessRecordReqDTO
import dev.haotangyuan.shortlink.dto.req.GroupStatsReqDTO
import dev.haotangyuan.shortlink.dto.req.LinkStatsAccessRecordReqDTO
import dev.haotangyuan.shortlink.dto.req.LinkStatsReqDTO
import dev.haotangyuan.shortlink.vo.LinkStatsAccessRecordVO
import dev.haotangyuan.shortlink.vo.LinkStatsVO
import dev.haotangyuan.shortlink.service.LinkStatsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 访问统计控制器
 *
 * @author haotangyuan
 */
@RestController
class LinkStatsController(private val linkStatsService: LinkStatsService) {

    /**
     * 访问单个短链接指定时间内监控数据
     *
     * @param linkStatsReqDTO 获取短链接监控数据入参
     * @return 短链接监控数据
     */
    @GetMapping("/api/short-link/v1/stats")
    fun shortLinkStats(linkStatsReqDTO: LinkStatsReqDTO): Result<LinkStatsVO?> =
        Results.success(linkStatsService.oneShortLinkStats(linkStatsReqDTO))

    /**
     * 访问分组短链接指定时间内监控数据
     *
     * @param groupStatsReqDTO 获取短链接监控数据入参
     * @return 短链接监控数据
     */
    @GetMapping("/api/short-link/v1/stats/group")
    fun groupShortLinkStats(groupStatsReqDTO: GroupStatsReqDTO): Result<LinkStatsVO?> =
        Results.success(linkStatsService.groupShortLinkStats(groupStatsReqDTO))

    /**
     * 访问单个短链接指定时间内访问记录监控数据
     *
     * @param linkStatsAccessRecordReqDTO 获取短链接监控访问记录数据入参
     * @return 访问记录监控数据
     */
    @GetMapping("/api/short-link/v1/stats/access-record")
    fun shortLinkStatsAccessRecord(
        linkStatsAccessRecordReqDTO: LinkStatsAccessRecordReqDTO,
    ): Result<IPage<LinkStatsAccessRecordVO>> =
        Results.success(linkStatsService.shortLinkStatsAccessRecord(linkStatsAccessRecordReqDTO))

    /**
     * 访问分组短链接指定时间内访问记录监控数据
     *
     * @param groupStatsAccessRecordReqDTO 获取分组短链接监控访问记录数据入参
     * @return 访问记录监控数据
     */
    @GetMapping("/api/short-link/v1/stats/access-record/group")
    fun groupShortLinkStatsAccessRecord(
        groupStatsAccessRecordReqDTO: GroupStatsAccessRecordReqDTO,
    ): Result<IPage<LinkStatsAccessRecordVO>> =
        Results.success(linkStatsService.groupShortLinkStatsAccessRecord(groupStatsAccessRecordReqDTO))
}
