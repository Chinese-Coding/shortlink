package dev.haotangyuan.shortlink.common.config;

import dev.haotangyuan.shortlink.mq.consumer.LinkStatsSaveConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

import static dev.haotangyuan.shortlink.common.constant.RedisKeyConstant.SHORT_LINK_STATS_STREAM_GROUP_KEY;
import static dev.haotangyuan.shortlink.common.constant.RedisKeyConstant.SHORT_LINK_STATS_STREAM_TOPIC_KEY;

/**
 * 短链访问统计的 Redis Stream 消费配置。
 * 容器从 Redis 拉取消息，LinkStatsSaveConsumer 处理统计并发送 ACK。
 * 投递后长期未确认的消息由 PendingMessageRecoveryTask 认领并重试。
 *
 * @author: haotangyuan
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class RedisStreamConfiguration {

    private final RedisConnectionFactory redisConnectionFactory;
    private final LinkStatsSaveConsumer linkStatsSaveConsumer;

    @Value("${short-link.stats.consumer-count:0}")
    private int configuredConsumerCount;

    @Value("${short-link.stats.consumer-instance-id:${HOSTNAME:local}}")
    private String consumerInstanceId;

    private static final long THROUGHPUT_LOG_INTERVAL_MS = 300_000L;
    private final LongAdder consumeCounter = new LongAdder();
    private final AtomicLong lastThroughputLogTime = new AtomicLong(System.currentTimeMillis());

    @Bean
    public ExecutorService asyncStreamConsumer() {
        // 线程池大小与订阅数一致，每个订阅可以独立轮询并处理消息。
        var consumerCount = consumerCount();
        var index = new AtomicInteger();
        log.info("消费者线程池配置，线程数：{}", consumerCount);
        return Executors.newFixedThreadPool(consumerCount, r -> {
            var t = new Thread(r, "stream_consumer_stats_" + index.getAndIncrement());
            t.setDaemon(true);
            return t;
        });
    }

    // Spring 初始化容器时启动监听，销毁容器时停止监听。
    @Bean(initMethod = "start", destroyMethod = "stop")
    public StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer(
            ExecutorService asyncStreamConsumer) {
        var options = StreamMessageListenerContainer
                .StreamMessageListenerContainerOptions
                .builder()
                .batchSize(100) // 每个订阅每次最多拉取 100 条
                .executor(asyncStreamConsumer)
                .pollTimeout(Duration.ofMillis(500)) // 暂无消息时，单次读取最多阻塞 500 毫秒
                .build();
        return StreamMessageListenerContainer.create(redisConnectionFactory, options);
    }

    /**
     * 每个订阅使用独立的消费者名，组内消费者分摊同一个 Stream 的消息。
     * 不同订阅并行调用同一个 LinkStatsSaveConsumer。
     */
    @Bean
    public List<Subscription> shortLinkStatsSaveConsumerSubscriptions(
            StreamMessageListenerContainer<String, MapRecord<String, String, String>> container) {

        var subscriptions = new ArrayList<Subscription>();
        var consumerCount = consumerCount();

        StreamListener<String, MapRecord<String, String, String>> loggingListener = message -> {
            linkStatsSaveConsumer.onMessage(message);
            // 统计正常返回的回调次数，包含 ACK 失败但在消费者内部捕获异常的情况。
            consumeCounter.increment();

            // 满 5 分钟后由下一次正常返回的回调触发日志，CAS 让同一轮只由一个线程输出。
            var now = System.currentTimeMillis();
            var last = lastThroughputLogTime.get();
            if (now - last >= THROUGHPUT_LOG_INTERVAL_MS && lastThroughputLogTime.compareAndSet(last, now)) {
                long processed = consumeCounter.sumThenReset();
                if (processed > 0) {
                    var elapsed = now - last;
                    var tps = processed * 1000 / elapsed;
                    log.info("Stream 消费吞吐量: {} msgs / {} ms (≈{} TPS)", processed, elapsed, tps);
                }
            }
        };

        // 消费者组由 LinkStatsStreamInitializeTask 创建，首次创建时从 0-0 开始。
        for (var i = 0; i < consumerCount; i++) {
            var request = StreamMessageListenerContainer
                    .StreamReadRequest
                    // 组模式下 lastConsumed() 对应 >，只读取尚未投递给组的消息。
                    .builder(StreamOffset.create(SHORT_LINK_STATS_STREAM_TOPIC_KEY, ReadOffset.lastConsumed()))
                    // 发生异常后保留订阅，未确认的消息留在 Pending，等待恢复任务重试。
                    .cancelOnError(_ -> false)
                    .consumer(Consumer.from(SHORT_LINK_STATS_STREAM_GROUP_KEY, "stats-consumer-" + consumerInstanceId + "-" + i))
                    // 由业务消费者在处理完成后显式 ACK，从组的待确认列表中移除消息。
                    .autoAcknowledge(false)
                    .build();

            var subscription = container.register(request, loggingListener);
            subscriptions.add(subscription);
            log.info("注册消费者: stats-consumer-{}-{}", consumerInstanceId, i);
        }

        return subscriptions;
    }

    // 优先使用显式配置。默认按 JVM 可用 CPU 数乘 1.5 取整，并限制在 1 到 8。
    private int consumerCount() {
        if (configuredConsumerCount > 0) {
            return configuredConsumerCount;
        }
        return Math.clamp((int) (Runtime.getRuntime().availableProcessors() * 1.5), 1, 8);
    }
}
