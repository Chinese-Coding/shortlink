package dev.haotangyuan.shortlink.toolkit

import cn.hutool.core.util.StrUtil
import dev.haotangyuan.shortlink.common.constant.RedisKeyConstant.Companion.SHORT_CODE_ALLOCATION_KEY
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference

/**
 * 极简短码生成器
 *
 * @author: haotangyuan
 */
object ShortCodeUtil {

    private val log = LoggerFactory.getLogger(ShortCodeUtil::class.java)

    /**
     * 字符集
     */
    private val CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray()

    /**
     * 参数（仅支持 6/7 位，运行期全用 long）
     */
    @Volatile
    private var length: Int = 0              // 6 or 7

    @Volatile
    private var segmentStep: Long = 0        // 号段步长

    @Volatile
    private var prefetchRatio: Double = 0.0  // 预取阈值占比 (0,1)

    @Volatile
    private var a: Long = 0                  // 仿射参数 a

    @Volatile
    private var b: Long = 0                  // 仿射参数 b

    @Volatile
    private var n: Long = 0                  // N=62^LENGTH

    /**
     * Redis 依赖
     */
    private lateinit var stringRedisTemplate: StringRedisTemplate

    /**
     * 当前段 [start, end] 与游标（包含）
     */
    @Volatile
    private var end: Long = 0 // start > end 表示“无段可用”
    private val cursor = AtomicLong(0)

    /**
     * 预取段（极简预取方案）
     */
    private val nextSeg = AtomicReference<Segment?>()
    private val prefetching = AtomicBoolean(false)

    private val LOCK = Any()

    private data class Segment(val start: Long, val end: Long)

    /*
     * 在应用启动时初始化（使用 ShortCodeProps）。
     * 仅支持 6/7 位；同步预热首个号段，避免首请求落慢路径。
     */
    @JvmStatic
    fun init(redisTemplate: StringRedisTemplate, props: ShortCodeProps) {
        check(!::stringRedisTemplate.isInitialized) { "ShortCodeUtil already initialized" }

        length = props.length ?: 6
        require(length == 6 || length == 7) { "shortcode length must be 6 or 7" }
        n = pow62(length)

        segmentStep = props.segmentStep ?: 100_000L
        prefetchRatio = props.prefetchRatio ?: 0.2
        require(segmentStep > 0) { "segmentStep must be > 0" }
        require(prefetchRatio > 0 && prefetchRatio < 1) { "prefetchRatio must be in (0,1)" }

        val aLong = props.a?.takeUnless { StrUtil.isBlank(it) }?.trim()?.toLong() ?: 1_999_997L
        require(isCoprime(aLong, 62)) { "affine 'a' must be odd and coprime to 62 and 31" }
        val aMax = (Long.MAX_VALUE - (n - 1)) / (n - 1)
        require(aLong in 1..aMax) { "affine 'a' too large for LONG safety; max=$aMax" }
        var bLong = props.b?.takeUnless { StrUtil.isBlank(it) }?.trim()?.toLong() ?: minOf(19_987_654_321L, n - 1)
        if (bLong < 0) bLong = (bLong % n + n) % n
        if (bLong >= n) bLong %= n
        a = aLong
        b = bLong

        stringRedisTemplate = redisTemplate
        // 启动预热：同步拉首个号段，避免首个请求落慢路径
        val first = fetchFromRedis()
        switchTo(first)
    }

    /* 生成下一个短码（固定 LENGTH 位），获取全局序号 i → 仿射置换为 y → Base62 固定长度编码 */
    @JvmStatic
    fun next(): String {
        ensureInit()
        val i = nextId()
        val y = mapIndexToY(i)
        return encodeBase62Fixed(y, length)
    }

    /**
     * 获取下一个全局自增 ID，并预取号段
     */
    private fun nextId(): Long {
        while (true) {
            val c = cursor.getAndIncrement()
            if (c <= end) {
                maybePrefetch()
                return c
            }

            // 无锁优先切换已预取段
            val prefetched = nextSeg.getAndSet(null)
            if (prefetched != null) {
                switchTo(prefetched)
                continue
            }

            // 慢路径：同步取段并切换（仅一个线程进入），无论是否切换，处理完都回到循环重试
            synchronized(LOCK) {
                if (cursor.get() <= end) {
                    return@synchronized
                }
                val seg = nextSeg.getAndSet(null) ?: fetchFromRedis()
                switchTo(seg)
            }
        }
    }

    /**
     * 触发异步预取下一个号段，剩余 prefetchRatio 时，预取
     */
    private fun maybePrefetch() {
        // 已有预取结果或预取在则不重复触发
        if (nextSeg.get() != null) return
        val rem = end - cursor.get() + 1
        if (rem > (segmentStep * prefetchRatio).toLong()) return
        if (!prefetching.compareAndSet(false, true)) return
        CompletableFuture.runAsync {
            try {
                val seg = fetchFromRedis()
                nextSeg.compareAndSet(null, seg)
            } catch (ex: Exception) {
                log.warn("Prefetch short-code segment failed: {}", ex.message)
            } finally {
                prefetching.set(false)
            }
        }
    }

    /**
     * 切换当前号段为指定段
     */
    private fun switchTo(seg: Segment) {
        end = seg.end
        cursor.set(seg.start)
    }

    /**
     * 从 Redis 获取一个新号段
     */
    private fun fetchFromRedis(): Segment {
        ensureInit()
        val value = stringRedisTemplate.opsForValue().increment(SHORT_CODE_ALLOCATION_KEY, segmentStep)
            ?: throw IllegalStateException("Redis INCRBY returned null")
        val newEnd = value - 1
        val newStart = newEnd - segmentStep + 1
        return Segment(newStart, newEnd)
    }

    /* Base62 固定长度编码（long 版，定长 char[] 从末位回填） */
    private fun encodeBase62Fixed(value: Long, len: Int): String {
        val out = CharArray(len)
        var v = value
        for (p in len - 1 downTo 0) {
            val idx = (v % 62).toInt()
            out[p] = CHARS[idx]
            v /= 62
        }
        return String(out)
    }

    /**
     * 确保已初始化
     */
    private fun ensureInit() {
        if (!::stringRedisTemplate.isInitialized || n <= 0 || length <= 0 || a == 0L) {
            throw IllegalStateException("ShortCodeUtil not initialized. Call ShortCodeUtil.init(...) at startup.")
        }
    }

    /**
     * 判断 a 与 b 是否互素
     */
    private fun isCoprime(a: Long, b: Int): Boolean {
        var x = a
        var y = b.toLong()
        while (y != 0L) {
            val t = x % y
            x = y
            y = t
        }
        return x == 1L
    }

    /* 62 的 n 次幂（n 仅为 6 或 7） */
    private fun pow62(n: Int): Long {
        var r = 1L
        for (i in 0 until n) r *= 62L
        return r
    }

    /* i → y 的仿射映射（long 版） */
    private fun mapIndexToY(i: Long): Long {
        var y = (a * i + b) % n
        if (y < 0) y += n
        return y
    }
}
