package dev.haotangyuan.shortlink.dao.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import dev.haotangyuan.shortlink.dao.entity.LinkFirstVisitDO
import org.apache.ibatis.annotations.Insert

/**
 * 首次访问判重表 Mapper
 *
 * @author haotangyuan
 */
interface LinkFirstVisitMapper : BaseMapper<LinkFirstVisitDO> {

    @Insert("""
            INSERT INTO t_link_first_visit (full_short_url, visitor_id) VALUES (#{fullShortUrl}, #{visitorId})
            ON CONFLICT (full_short_url, visitor_id) DO NOTHING
            """)
    fun insertIgnore(entity: LinkFirstVisitDO): Int
}
