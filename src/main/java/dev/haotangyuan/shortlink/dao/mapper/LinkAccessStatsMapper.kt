package dev.haotangyuan.shortlink.dao.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import dev.haotangyuan.shortlink.dao.entity.LinkAccessStatsDO
import dev.haotangyuan.shortlink.dto.req.GroupStatsReqDTO
import dev.haotangyuan.shortlink.dto.req.LinkStatsReqDTO
import dev.haotangyuan.shortlink.vo.GroupLinkCountQueryVO
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

import java.util.Date

/**
 * 短链接基础访问统计持久层
 *
 * @author haotangyuan
 */
interface LinkAccessStatsMapper : BaseMapper<LinkAccessStatsDO> {

    /**
     * 记录基础访问监控数据
     *
     * @param linkAccessStatsDO 访问统计实体
     */
    @Insert("""
            INSERT INTO t_link_access_stats (
                full_short_url, stats_date, pv, uv, uip, stats_hour, stats_weekday, create_time, update_time, del_flag
            )
            VALUES (
                #{linkAccessStats.fullShortUrl},
                #{linkAccessStats.date},
                #{linkAccessStats.pv},
                #{linkAccessStats.uv},
                #{linkAccessStats.uip},
                #{linkAccessStats.hour},
                #{linkAccessStats.weekday},
                NOW(), NOW(), 0
            )
            ON CONFLICT (full_short_url, stats_date, stats_hour) DO UPDATE SET
                pv = t_link_access_stats.pv + EXCLUDED.pv,
                uv = t_link_access_stats.uv + EXCLUDED.uv,
                uip = t_link_access_stats.uip + EXCLUDED.uip
            """)
    fun shortLinkAccessStats(@Param("linkAccessStats") linkAccessStatsDO: LinkAccessStatsDO)

    /**
     * 根据短链接获取指定日期内基础监控数据
     *
     * @param linkStatsReqDTO 查询参数
     * @return 基础访问统计列表
     */
    @Select("""
            SELECT
                tlas.stats_date AS "date",
                SUM(tlas.pv)  AS pv,
                SUM(tlas.uv)  AS uv,
                SUM(tlas.uip) AS uip
            FROM t_link tl
            INNER JOIN t_link_access_stats tlas
                ON tl.full_short_url = tlas.full_short_url
            WHERE tlas.full_short_url = #{param.fullShortUrl}
              AND tl.gid = #{param.gid}
              AND tl.del_flag = '0'
              AND tl.enable_status = #{param.enableStatus}
              AND tlas.stats_date BETWEEN #{param.startDate} AND #{param.endDate}
            GROUP BY tlas.full_short_url, tl.gid, tlas.stats_date
            """)
    fun listStatsByShortLink(@Param("param") linkStatsReqDTO: LinkStatsReqDTO): List<LinkAccessStatsDO>

    /**
     * 根据短链接获取指定日期内小时基础监控数据
     *
     * @param linkStatsReqDTO 查询参数
     * @return 基础访问统计列表
     */
    @Select("""
            SELECT
                tlas.stats_hour AS "hour",
                SUM(tlas.pv) AS pv
            FROM t_link tl
            INNER JOIN t_link_access_stats tlas
                ON tl.full_short_url = tlas.full_short_url
            WHERE tlas.full_short_url = #{param.fullShortUrl}
              AND tl.gid = #{param.gid}
              AND tl.del_flag = '0'
              AND tl.enable_status = #{param.enableStatus}
              AND tlas.stats_date BETWEEN #{param.startDate} AND #{param.endDate}
            GROUP BY tlas.full_short_url, tl.gid, tlas.stats_hour
            """)
    fun listHourStatsByShortLink(@Param("param") linkStatsReqDTO: LinkStatsReqDTO): List<LinkAccessStatsDO>

    /**
     * 根据短链接获取指定日期内小时基础监控数据
     *
     * @param linkStatsReqDTO 查询参数
     * @return 基础访问统计列表
     */
    @Select("""
            SELECT
                tlas.stats_weekday AS "weekday",
                SUM(tlas.pv) AS pv
            FROM t_link tl
            INNER JOIN t_link_access_stats tlas
                ON tl.full_short_url = tlas.full_short_url
            WHERE tlas.full_short_url = #{param.fullShortUrl}
              AND tl.gid = #{param.gid}
              AND tl.del_flag = '0'
              AND tl.enable_status = #{param.enableStatus}
              AND tlas.stats_date BETWEEN #{param.startDate} AND #{param.endDate}
            GROUP BY tlas.full_short_url, tl.gid, tlas.stats_weekday
            """)
    fun listWeekdayStatsByShortLink(@Param("param") linkStatsReqDTO: LinkStatsReqDTO): List<LinkAccessStatsDO>

    /**
     * 根据分组获取指定日期内基础监控数据
     *
     * @param groupStatsReqDTO 查询参数
     * @return 基础访问统计列表
     */
    @Select("""
            SELECT
                tlas.stats_date AS "date",
                SUM(tlas.pv)  AS pv,
                SUM(tlas.uv)  AS uv,
                SUM(tlas.uip) AS uip
            FROM t_link tl
            INNER JOIN t_link_access_stats tlas
                    ON tl.full_short_url = tlas.full_short_url
            WHERE tl.gid = #{param.gid}
              AND tl.del_flag = '0'
              AND tl.enable_status = '0'
              AND tlas.stats_date BETWEEN #{param.startDate} AND #{param.endDate}
            GROUP BY tl.gid, tlas.stats_date
            """)
    fun listStatsByGroup(@Param("param") groupStatsReqDTO: GroupStatsReqDTO): List<LinkAccessStatsDO>

    /**
     * 根据分组获取指定日期内小时基础监控数据
     *
     * @param groupStatsReqDTO 查询参数
     * @return 基础访问统计列表
     */
    @Select("""
            SELECT
                tls.stats_hour AS "hour",
                SUM(tls.pv) AS pv
            FROM
                t_link tl
                INNER JOIN t_link_access_stats tls
                    ON tl.full_short_url = tls.full_short_url
            WHERE
                tl.gid = #{param.gid}
                AND tl.del_flag = '0'
                AND tl.enable_status = '0'
                AND tls.stats_date BETWEEN #{param.startDate} AND #{param.endDate}
            GROUP BY
                tl.gid,
                tls.stats_hour
            """)
    fun listHourStatsByGroup(@Param("param") groupStatsReqDTO: GroupStatsReqDTO): List<LinkAccessStatsDO>

    /**
     * 根据分组获取指定日期内星期基础监控数据
     *
     * @param groupStatsReqDTO 查询参数
     * @return 基础访问统计列表
     */
    @Select("""
            SELECT
                tlas.stats_weekday AS "weekday",
                SUM(tlas.pv) AS pv
            FROM t_link tl
            INNER JOIN t_link_access_stats tlas
                ON tl.full_short_url = tlas.full_short_url
            WHERE tl.gid = #{param.gid}
              AND tl.del_flag = '0'
              AND tl.enable_status = '0'
              AND tlas.stats_date BETWEEN #{param.startDate} AND #{param.endDate}
            GROUP BY tl.gid, tlas.stats_weekday
            """)
    fun listWeekdayStatsByGroup(@Param("param") groupStatsReqDTO: GroupStatsReqDTO): List<LinkAccessStatsDO>

    /**
     * 批量查询短链接今日 PV。
     */
    @Select("""
            <script>
            SELECT
                tlas.full_short_url,
                COALESCE(SUM(tlas.pv), 0) AS pv
            FROM t_link_access_stats tlas
            WHERE tlas.full_short_url IN
              <foreach item="fullShortUrl" collection="fullShortUrls" open="(" separator="," close=")">
                #{fullShortUrl}
              </foreach>
              AND tlas.del_flag = '0'
              AND tlas.stats_date = #{today}
            GROUP BY tlas.full_short_url
            </script>
            """)
    fun listTodayPvByShortUrls(
        @Param("fullShortUrls") fullShortUrls: List<String>,
        @Param("today") today: Date,
    ): List<LinkAccessStatsDO>

    /**
     * 批量查询分组今日 PV。
     */
    @Select("""
            <script>
            SELECT
                tl.gid AS gid,
                COALESCE(SUM(tlas.pv), 0) AS "todayPv"
            FROM t_link tl
            INNER JOIN t_link_access_stats tlas
                ON tl.full_short_url = tlas.full_short_url
            WHERE tl.gid IN
              <foreach item="gid" collection="gidList" open="(" separator="," close=")">
                #{gid}
              </foreach>
              AND tl.del_flag = '0'
              AND tl.enable_status = '0'
              AND tlas.del_flag = '0'
              AND tlas.stats_date = #{today}
            GROUP BY tl.gid
            </script>
            """)
    fun listTodayPvByGroups(
        @Param("gidList") gidList: List<String>,
        @Param("today") today: Date,
    ): List<GroupLinkCountQueryVO>

}
