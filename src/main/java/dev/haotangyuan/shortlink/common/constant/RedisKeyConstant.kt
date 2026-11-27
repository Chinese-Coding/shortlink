package dev.haotangyuan.shortlink.common.constant

/**
 * Redis Key 常量类
 *
 * @author haotangyuan
 */
open class RedisKeyConstant {
    companion object {
        /**
         * 用户注册分布式锁
         * 格式：short-link:lock:user-register:{username}
         */
        const val LOCK_USER_REGISTER_KEY: String = "short-link:lock:user-register:"

        /**
         * 用户登录缓存标识
         * 格式：short-link:login:{token}
         */
        const val USER_LOGIN_KEY: String = "short-link:login:"

        /**
         * 分组创建分布式锁
         * 格式：short-link:lock:group-create:{username}
         */
        const val LOCK_GROUP_CREATE_KEY: String = "short-link:lock:group-create:%s"

        /**
         * 短链接跳转前缀 key
         * 格式：short-link:goto:{fullShortUrl}
         */
        const val GOTO_SHORT_LINK_KEY: String = "short-link:goto:%s"

        /**
         * 短链接空值跳转锁前缀 key
         * 格式：short-link:is-null:goto_{fullShortUrl}
         */
        const val GOTO_IS_NULL_SHORT_LINK_KEY: String = "short-link:is-null:goto_%s"

        /**
         * 短链接跳转锁前缀 key
         * 格式：short-link:lock:goto:{fullShortUrl}
         */
        const val LOCK_GOTO_SHORT_LINK_KEY: String = "short-link:lock:goto:%s"

        /**
         * 短链接修改分组 ID 锁前缀 Key
         * 格式：short-link:lock:update-gid:{fullShortUrl}
         */
        const val LOCK_GID_UPDATE_KEY: String = "short-link:lock:update-gid:%s"

        /**
         * 短码号段分配全局 Key
         * 格式：short-link:allocation:global
         */
        const val SHORT_CODE_ALLOCATION_KEY: String = "short-link:allocation:global"

        /**
         * 短链接幂等操作锁前缀 key
         * 格式：short-link:idempotent:{messageId}
         */
        const val IDEMPOTENT_KEY_PREFIX: String = "short-link:idempotent:%s"

        /**
         * 短链接监控消息保存队列 Topic 缓存标识
         */
        const val SHORT_LINK_STATS_STREAM_TOPIC_KEY: String = "short-link:stats-stream"

        /**
         * 短链接监控消息保存队列 Group 缓存标识
         */
        const val SHORT_LINK_STATS_STREAM_GROUP_KEY: String = "short-link:stats-stream:only-group"

        /**
         * 用户 GID 正向索引集合
         * 格式：short-link:user-gids:{username} (Set of gid)
         */
        const val USER_GIDS_KEY: String = "short-link:user-gids:%s"

        /**
         * Admin / Client 会话 Token 映射
         * 格式：short-link:session:{token} -> username
         */
        const val SESSION_KEY: String = "short-link:session:%s"

        /**
         * Core API 访问令牌映射
         * 明文映射（旧方案，逐步废弃）：short-link:api-token:{token} -> username
         */
        const val API_TOKEN_KEY_PREFIX: String = "short-link:api-token:%s"

        /**
         * Core API 访问令牌映射（哈希值）
         * 格式：short-link:api-token-h:{sha256(token)} -> username
         */
        const val API_TOKEN_HASH_KEY: String = "short-link:api-token-h:%s"

        /**
         * 短链接 UV HyperLogLog 缓存标识
         * 格式：short-link:stats:uv:{v}:{fullShortUrl}
         * v = epochDay(Asia/Shanghai) % 2
         */
        const val STATS_UV_HLL_KEY: String = "short-link:stats:uv:%d:%s"

        /**
         * UV HLL 前缀（批量 PFCOUNT 使用）
         * 格式：short-link:stats:uv:{v}:
         */
        const val STATS_UV_PREFIX: String = "short-link:stats:uv:%d:"

        /**
         * 短链接 UIP HyperLogLog 缓存标识
         * 格式：short-link:stats:uip:{v}:{fullShortUrl}
         * v = epochDay(Asia/Shanghai) % 2
         */
        const val STATS_UIP_HLL_KEY: String = "short-link:stats:uip:%d:%s"

        /**
         * UIP HLL 前缀（批量 PFCOUNT 使用）
         * 格式：short-link:stats:uip:{v}:
         */
        const val STATS_UIP_PREFIX: String = "short-link:stats:uip:%d:"

        /**
         * UV 活跃集合 key（用于清理）
         * 格式：short-link:stats:uv:active:{v}
         * v = epochDay(Asia/Shanghai) % 2
         */
        const val STATS_UV_ACTIVE_KEY: String = "short-link:stats:uv:active:%d"

        /**
         * UIP 活跃集合 key（用于清理）
         * 格式：short-link:stats:uip:active:{v}
         * v = epochDay(Asia/Shanghai) % 2
         */
        const val STATS_UIP_ACTIVE_KEY: String = "short-link:stats:uip:active:%d"

        /**
         * 统计消息对应的 HLL 增量暂存。
         * 格式：short-link:stats:hll-delta:{messageId}:{dimension}
         */
        const val STATS_HLL_DELTA_KEY: String = "short-link:stats:hll-delta:%s:%s"
    }
}
