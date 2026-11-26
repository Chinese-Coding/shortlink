package dev.haotangyuan.shortlink.dao.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import dev.haotangyuan.shortlink.dao.entity.LinkDO
import dev.haotangyuan.shortlink.dto.req.LinkPageReqDTO
import dev.haotangyuan.shortlink.vo.GroupLinkCountQueryVO
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

/**
 * 短链接持久层
 *
 * @author haotangyuan
 */
interface LinkMapper : BaseMapper<LinkDO> {

    /**
     * 增量更新短链接统计数据
     *
     * @return 受影响的行数
     */
    @Update("""
            UPDATE t_link
            SET total_pv = total_pv + #{totalPv},
                total_uv = total_uv + #{totalUv},
                total_uip = total_uip + #{totalUip}
            WHERE gid = #{gid}
              AND full_short_url = #{fullShortUrl}
              AND del_flag = 0
            """)
    fun incrementStats(
        @Param("gid") gid: String,
        @Param("fullShortUrl") fullShortUrl: String,
        @Param("totalPv") totalPv: Int?,
        @Param("totalUv") totalUv: Int?,
        @Param("totalUip") totalUip: Int?,
    ): Int

    /**
     * 分页统计短链接
     */
    @Select("""
            <script>
            SELECT
                t.*
            FROM t_link t
            WHERE t.gid = #{p.gid}
              AND t.enable_status = 0
              AND t.del_flag = 0
            <if test="p.keyword != null and p.keyword != ''">
                <bind name="keywordPattern" value="'%' + p.keyword + '%'" />
                AND (
                    t.full_short_url ILIKE #{keywordPattern}
                    OR t.origin_url ILIKE #{keywordPattern}
                    OR t."describe" ILIKE #{keywordPattern}
                )
            </if>
            <choose>
                <when test="p.orderTag == 'totalPv'">ORDER BY t.total_pv DESC</when>
                <when test="p.orderTag == 'totalUv'">ORDER BY t.total_uv DESC</when>
                <when test="p.orderTag == 'totalUip'">ORDER BY t.total_uip DESC</when>
                <otherwise>ORDER BY t.create_time DESC</otherwise>
            </choose>
            </script>
            """)
    fun pageLink(page: Page<LinkDO>, @Param("p") dto: LinkPageReqDTO): IPage<LinkDO>

    /**
     * 查询分组短链接数量
     */
    @Select("""
            <script>
            SELECT
                gid AS gid,
                COUNT(*) AS "linkCount",
                COALESCE(SUM(total_pv), 0) AS "totalPv"
            FROM t_link
            WHERE gid IN
            <foreach item="gid" collection="gidList" open="(" separator="," close=")">
                #{gid}
            </foreach>
              AND enable_status = 0
              AND del_flag = 0
            GROUP BY gid
            </script>
            """)
    fun listGroupLinkCount(@Param("gidList") gidList: List<String>): List<GroupLinkCountQueryVO>
}
