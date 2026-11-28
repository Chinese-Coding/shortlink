package dev.haotangyuan.shortlink.dao.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import dev.haotangyuan.shortlink.dao.entity.LinkAccessLogsDO
import dev.haotangyuan.shortlink.dao.entity.LinkAccessStatsDO
import dev.haotangyuan.shortlink.dto.req.GroupStatsAccessRecordReqDTO
import dev.haotangyuan.shortlink.dto.req.GroupStatsReqDTO
import dev.haotangyuan.shortlink.dto.req.LinkStatsAccessRecordReqDTO
import dev.haotangyuan.shortlink.dto.req.LinkStatsReqDTO
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

import java.util.HashMap

/**
 * 短链接访问日志持久层
 *
 * @author haotangyuan
 */
interface LinkAccessLogsMapper : BaseMapper<LinkAccessLogsDO> {

    /**
     * 根据短链接获取指定日期内 PV UV UIP 数据
     *
     * @param linkStatsReqDTO 统计请求参数
     * @return 访问统计数据
     */
    @Select("""
            SELECT
                COUNT(tlal.visitor_id) AS pv,
                COUNT(DISTINCT tlal.visitor_id) AS uv,
                COUNT(DISTINCT tlal.ip) AS uip
            FROM t_link tl
            INNER JOIN t_link_access_logs tlal ON tl.full_short_url = tlal.full_short_url
            WHERE tlal.full_short_url = #{param.fullShortUrl}
              AND tl.gid = #{param.gid}
              AND tl.del_flag = '0'
              AND tl.enable_status = #{param.enableStatus}
              AND tlal.create_time BETWEEN #{param.startDate} AND #{param.endDate}
            GROUP BY tlal.full_short_url, tl.gid
            """)
    fun findPvUvUidStatsByShortLink(@Param("param") linkStatsReqDTO: LinkStatsReqDTO): LinkAccessStatsDO?

    /**
     * 根据分组获取指定日期内PV、UV、UIP数据
     */
    @Select("""
            SELECT
                COUNT(tls.visitor_id) AS pv,
                COUNT(DISTINCT tls.visitor_id) AS uv,
                COUNT(DISTINCT tls.ip) AS uip
            FROM t_link tl
            INNER JOIN t_link_access_logs tls
                ON tl.full_short_url = tls.full_short_url
            WHERE tl.gid = #{param.gid}
                AND tl.del_flag = '0'
                AND tl.enable_status = '0'
                AND tls.create_time BETWEEN #{param.startDate} AND #{param.endDate}
            GROUP BY tl.gid
            """)
    fun findPvUvUidStatsByGroup(@Param("param") groupStatsReqDTO: GroupStatsReqDTO): LinkAccessStatsDO?

    /**
     * 根据短链接获取指定日期内高频访问IP数据
     *
     * @param linkStatsReqDTO 统计请求参数
     * @return 高频访问IP列表
     */
    @Select("""
            SELECT
                tls.ip,
                COUNT(tls.ip) AS count
            FROM t_link tl
            INNER JOIN t_link_access_logs tls
                ON tl.full_short_url = tls.full_short_url
            WHERE tls.full_short_url = #{param.fullShortUrl}
                AND tl.gid = #{param.gid}
                AND tl.del_flag = '0'
                AND tl.enable_status = #{param.enableStatus}
                AND tls.create_time BETWEEN #{param.startDate} AND #{param.endDate}
            GROUP BY tls.full_short_url
                , tl.gid
                , tls.ip
            ORDER BY count DESC
            LIMIT 5
            """)
    fun listTopIpByShortLink(@Param("param") linkStatsReqDTO: LinkStatsReqDTO): List<HashMap<String, Any>>

    /**
     * 根据分组获取指定日期内高频访问IP数据
     *
     * @param groupStatsReqDTO 统计请求参数
     * @return 高频访问IP列表
     */
    @Select("""
            SELECT
                tls.ip,
                COUNT(tls.ip) AS count
            FROM t_link tl
            INNER JOIN t_link_access_logs tls
                ON tl.full_short_url = tls.full_short_url
            WHERE tl.gid = #{param.gid}
                AND tl.del_flag = '0'
                AND tl.enable_status = '0'
                AND tls.create_time BETWEEN #{param.startDate} AND #{param.endDate}
            GROUP BY tl.gid
                , tls.ip
            ORDER BY count DESC
            LIMIT 5
            """)
    fun listTopIpByGroup(@Param("param") groupStatsReqDTO: GroupStatsReqDTO): List<HashMap<String, Any>>

    /**
     * 根据短链接获取指定日期内新旧访客数据
     *
     * @param linkStatsReqDTO 统计请求参数
     * @return 新旧访客统计数据
     */
    @Select("""
            SELECT
                COUNT(DISTINCT CASE WHEN tls.first_flag = TRUE THEN tls.visitor_id END) AS "newUserCnt",
                COUNT(DISTINCT CASE WHEN tls.first_flag = FALSE THEN tls.visitor_id END) AS "oldUserCnt"
            FROM t_link tl
            INNER JOIN t_link_access_logs tls
                ON tl.full_short_url = tls.full_short_url
            WHERE tls.full_short_url = #{param.fullShortUrl}
                AND tl.gid = #{param.gid}
                AND tl.enable_status = #{param.enableStatus}
                AND tl.del_flag = '0'
                AND tls.del_flag = '0'
                AND tls.create_time BETWEEN #{param.startDate} AND #{param.endDate}
            """)
    fun findUvTypeCntByShortLink(@Param("param") linkStatsReqDTO: LinkStatsReqDTO): HashMap<String, Any>?

    /**
     * 根据短链接和分组分页获取访问日志。
     */
    @Select("""
            SELECT
                tls.*
            FROM t_link tl
            INNER JOIN t_link_access_logs tls
                ON tl.full_short_url = tls.full_short_url
            WHERE tls.full_short_url = #{param.fullShortUrl}
                AND tl.gid = #{param.gid}
                AND tl.del_flag = '0'
                AND tl.enable_status = #{param.enableStatus}
                AND tls.del_flag = '0'
                AND tls.create_time BETWEEN #{param.startDate} AND #{param.endDate}
            ORDER BY tls.create_time DESC
            """)
    fun selectLinkPage(
        page: Page<LinkAccessLogsDO>,
        @Param("param") request: LinkStatsAccessRecordReqDTO,
    ): IPage<LinkAccessLogsDO>

    /**
     * 根据短链接分页获取访问日志
     *
     * @param groupStatsAccessRecordReqDTO 查询参数
     * @return 访问日志分页数据
     */
    @Select("""
            SELECT
                tls.*
            FROM t_link tl
            INNER JOIN t_link_access_logs tls
                ON tl.full_short_url = tls.full_short_url
            WHERE tl.gid = #{param.gid}
                AND tl.del_flag = '0'
                AND tl.enable_status = '0'
                AND tls.del_flag = '0'
                AND tls.create_time BETWEEN #{param.startDate} AND #{param.endDate}
            ORDER BY tls.create_time DESC
            """)
    fun selectGroupPage(
        page: Page<LinkAccessLogsDO>,
        @Param("param") groupStatsAccessRecordReqDTO: GroupStatsAccessRecordReqDTO,
    ): IPage<LinkAccessLogsDO>

}
