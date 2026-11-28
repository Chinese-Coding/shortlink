package dev.haotangyuan.shortlink

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * @author haotangyuan
 */
@SpringBootApplication
@EnableScheduling
@MapperScan("dev.haotangyuan.shortlink.dao.mapper")
class ShortLinkApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(ShortLinkApplication::class.java, *args)
        }
    }
}
