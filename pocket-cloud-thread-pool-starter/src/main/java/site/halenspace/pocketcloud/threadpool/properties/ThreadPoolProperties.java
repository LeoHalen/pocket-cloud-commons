package site.halenspace.pocketcloud.threadpool.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import site.halenspace.pocketcloud.threadpool.consts.QueueTypeConst;
import site.halenspace.pocketcloud.threadpool.consts.RejectedExecutionHandlerTypeConst;

import java.util.concurrent.TimeUnit;

/**
 * 线程池参数配置
 *
 * @Author Halen.Leo · 2021/6/30
 * @Blogger 后起小生
 * @Github https://github.com/LeoHalen
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = "pocket.dynamic-thread-pool")
public class ThreadPoolProperties {

    /**
     * 线程池名称, 默认值为 "dynamic-thread-pool"
     */
    private String threadPoolName = "dynamic-thread-pool";

    /**
     * 核心线程数, 默认值为CPU核心数量
     */
    private int corePoolSize = Runtime.getRuntime().availableProcessors();

    /**
     * 最大线程数, 默认值为(2 * CPU核心数量)
     */
    private int maximumPoolSize = 2 * Runtime.getRuntime().availableProcessors();

    /**
     * 空闲线程存活时间
     */
    private long keepAliveTime;

    /**
     * 空闲线程存活时间单位, 默认为毫秒
     * @See TimeUnit
     */
    private TimeUnit timeUnit = TimeUnit.MILLISECONDS;

    /**
     * 队列最大数量
     */
    private int queueCapacity = Integer.MAX_VALUE;

    /**
     * 队列类型
     * @see QueueTypeConst
     */
    private String queueType = QueueTypeConst.LinkedBlockingQueue;

    /**
     * SynchronousQueue 是否公平策略
     */
    private boolean fair;

    /**
     * 拒绝策略, 默认丢弃任务并抛出异常
     * @see RejectedExecutionHandlerTypeConst
     */
    private String rejectedExecutionType = RejectedExecutionHandlerTypeConst.AbortPolicy;

    /**
     * 队列容量告警阀值, 默认为队列容量
     */
    private int queueCapacityThreshold = queueCapacity;
}
