package dev.haotangyuan.shortlink.common.config

import com.baomidou.mybatisplus.annotation.DbType
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author haotangyuan
 */
@Configuration
open class DataBaseConfiguration {
    /** 分页插件 */
    @Bean
    open fun myBatisPlusInterceptor(): MybatisPlusInterceptor {
        val interceptor = MybatisPlusInterceptor()
        interceptor.addInnerInterceptor(PaginationInnerInterceptor(DbType.POSTGRE_SQL))
        return interceptor
    }
}
