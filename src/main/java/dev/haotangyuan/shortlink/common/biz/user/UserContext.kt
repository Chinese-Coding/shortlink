package dev.haotangyuan.shortlink.common.biz.user

import com.alibaba.ttl.TransmittableThreadLocal

/** 用户上下文：仅保存 username */
open class UserContext {
    companion object {
        private val usernameHolder: ThreadLocal<String?> = TransmittableThreadLocal()

        /** 设置用户名到上下文 */
        @JvmStatic
        fun setUsername(username: String?) {
            usernameHolder.set(username)
        }

        /** 获取上下文中的用户名 */
        @JvmStatic
        fun getUsername(): String? = usernameHolder.get()

        /** 清理上下文 */
        @JvmStatic
        fun removeUser() {
            usernameHolder.remove()
        }
    }
}
