package site.halenspace.pocketcloud.threadpool.strategy.properties;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import site.halenspace.pocketcloud.threadpool.DynamicThreadPoolKey;
import site.halenspace.pocketcloud.threadpool.consts.QueueTypeConst;
import site.halenspace.pocketcloud.threadpool.consts.RejectedExecutionHandlerTypeConst;
import site.halenspace.pocketcloud.threadpool.strategy.properties.archaius.ThreadPoolPropertiesChainedProperty;
import site.halenspace.pocketcloud.threadpool.strategy.properties.property.ThreadPoolProperty;

import java.util.concurrent.TimeUnit;

/**
 * 线程池参数配置
 *
 * @author Halen Leo · 2021/6/30
 * @Blogger 后起小生
 * @Github https://github.com/LeoHalen
 */
@Slf4j
@Data
//@ConfigurationProperties(prefix = "pocket.dynamic-thread-pool")
public class DynamicThreadPoolProperties {

    /**
     * 线程池名称, 默认值为 "dynamic-thread-pool"
     */
    private String defaultThreadPoolName = "dynamic-thread-pool";

    /**
     * 核心线程数, 默认值为CPU核心数量
     */
    private int defaultCorePoolSize = Runtime.getRuntime().availableProcessors();

    /**
     * 最大线程数, 默认值为(2 * CPU核心数量)
     */
    private int defaultMaximumPoolSize = 2 * Runtime.getRuntime().availableProcessors();

    /**
     * 空闲线程存活时间
     */
    private long defaultKeepAliveTime = 60;

    /**
     * 空闲线程存活时间单位, 默认为秒
     * @See TimeUnit
     */
    private TimeUnit defaultTimeUnit = TimeUnit.SECONDS;

    /**
     * 队列最大数量
     */
    private int defaultMaxQueueSize = Integer.MAX_VALUE;

    /**
     * 队列类型
     * @see QueueTypeConst
     */
    private String defaultQueueType = QueueTypeConst.LinkedBlockingQueue;

    /**
     * SynchronousQueue 是否公平策略
     */
    private boolean defaultFair = Boolean.FALSE;

    /**
     * 拒绝策略, 默认丢弃任务并抛出异常
     * @see RejectedExecutionHandlerTypeConst
     */
    private String defaultRejectedExecutionType = RejectedExecutionHandlerTypeConst.AbortPolicy;

    /**
     * 队列容量告警阀值, 默认为队列容量
     */
    private int defaultQueueSizeRejectionThreshold = (int) (defaultMaxQueueSize * 0.8);

    /**
     * 懒加载模式开关, 默认打开
     * @Describe 懒加载模式打开后，线程池执行器的初始化时机为第一次获取时
     */
    private boolean lazyModeEnabled = true;


    private final ThreadPoolProperty<Integer> corePoolSize;
    private final ThreadPoolProperty<Integer> maximumPoolSize;
    private final ThreadPoolProperty<Long> keepAliveTime;
    private final ThreadPoolProperty<String> queueType;
    private final ThreadPoolProperty<Integer> maxQueueSize;
    private final ThreadPoolProperty<Boolean> fair;
    private final ThreadPoolProperty<Integer> queueSizeRejectionThreshold;
    private final ThreadPoolProperty<String> rejectedExecutionType;
//    private final ThreadPoolProperty<Boolean> allowMaximumSizeToDivergeFromCoreSize;
//    private final ThreadPoolProperty<Integer> threadPoolRollingNumberStatisticalWindowInMilliseconds;
//    private final ThreadPoolProperty<Integer> threadPoolRollingNumberStatisticalWindowBuckets;


    protected DynamicThreadPoolProperties(DynamicThreadPoolKey key) {
        this(key, new Setter(), "dynamic");
    }

    protected DynamicThreadPoolProperties(DynamicThreadPoolKey key, Setter builder) {
        this(key, builder, "dynamic");
    }

    protected DynamicThreadPoolProperties(DynamicThreadPoolKey key, Setter builder, String propertyPrefix) {
        this.corePoolSize = getProperty(propertyPrefix, key, "corePoolSize", builder.getCorePoolSize(), defaultCorePoolSize);
        //this object always contains a reference to the configuration value for the maximumSize of the threadpool
        //it only gets applied if allowMaximumSizeToDivergeFromCoreSize is true
        this.maximumPoolSize = getProperty(propertyPrefix, key, "maximumPoolSize", builder.getMaximumPoolSize(), defaultMaximumPoolSize);
        this.keepAliveTime = getProperty(propertyPrefix, key, "keepAliveTime", builder.getKeepAliveTime(), defaultKeepAliveTime);
        this.queueType = getProperty(propertyPrefix, key, "queueType", builder.getQueueType(), defaultQueueType);
        this.maxQueueSize = getProperty(propertyPrefix, key, "maxQueueSize", builder.getMaxQueueSize(), defaultMaxQueueSize);
        this.fair = getProperty(propertyPrefix, key, "fair", builder.getFair(), defaultFair);
        this.queueSizeRejectionThreshold = getProperty(propertyPrefix, key, "queueSizeRejectionThreshold", builder.getQueueSizeRejectionThreshold(), defaultQueueSizeRejectionThreshold);
        this.rejectedExecutionType = getProperty(propertyPrefix, key, "rejectedExecutionType", builder.getRejectedExecutionType(), defaultRejectedExecutionType);
    }

    private static ThreadPoolProperty<Integer> getProperty(String propertyPrefix, DynamicThreadPoolKey key, String instanceProperty, Integer builderOverrideValue, Integer defaultValue) {
        return ThreadPoolPropertiesChainedProperty.forInteger()
                .add(propertyPrefix + ".threadpool." + key.name() + "." + instanceProperty, builderOverrideValue)
                .add(propertyPrefix + ".threadpool.default." + instanceProperty, defaultValue)
                .build();
    }

    private static ThreadPoolProperty<Boolean> getProperty(String propertyPrefix, DynamicThreadPoolKey key, String instanceProperty, Boolean builderOverrideValue, Boolean defaultValue) {
        return ThreadPoolPropertiesChainedProperty.forBoolean()
                .add(propertyPrefix + ".threadpool." + key.name() + "." + instanceProperty, builderOverrideValue)
                .add(propertyPrefix + ".threadpool.default." + instanceProperty, defaultValue)
                .build();
    }

    private static ThreadPoolProperty<Long> getProperty(String propertyPrefix, DynamicThreadPoolKey key, String instanceProperty, Long builderOverrideValue, Long defaultValue) {
        return ThreadPoolPropertiesChainedProperty.forLong()
                .add(propertyPrefix + ".threadpool." + key.name() + "." + instanceProperty, builderOverrideValue)
                .add(propertyPrefix + ".threadpool.default." + instanceProperty, defaultValue)
                .build();
    }

    private static ThreadPoolProperty<String> getProperty(String propertyPrefix, DynamicThreadPoolKey key, String instanceProperty, String builderOverrideValue, String defaultValue) {
        return ThreadPoolPropertiesChainedProperty.forString()
                .add(propertyPrefix + ".threadpool." + key.name() + "." + instanceProperty, builderOverrideValue)
                .add(propertyPrefix + ".threadpool.default." + instanceProperty, defaultValue)
                .build();
    }

    /**
     * Factory method to retrieve the default Setter.
     */
    public static Setter Setter() {
        return new Setter();
    }

    /**
     * 配置属性构建类
     */
    @Getter
    public static class Setter {

        private String threadPoolName = null;
        private Integer corePoolSize = null;
        private Integer maximumPoolSize = null;
        private Long keepAliveTime = null;
        private TimeUnit timeUnit = null;
        private Integer maxQueueSize = null;
        private String queueType = null;
        private Boolean fair;
        private String rejectedExecutionType = null;
        private Integer queueSizeRejectionThreshold = null;

        /* package */ Setter() {
        }

        public Setter withThreadPoolName(String value) {
            this.threadPoolName = value;
            return this;
        }

        public Setter withCorePoolSize(int value) {
            this.corePoolSize = value;
            return this;
        }

        public Setter withMaximumPoolSize(int value) {
            this.maximumPoolSize = value;
            return this;
        }

        public Setter withKeepAliveTime(long value) {
            this.keepAliveTime = value;
            return this;
        }

        public Setter withTimeUnit(TimeUnit value) {
            this.timeUnit = value;
            return this;
        }

        public Setter withMaxQueueSize(int value) {
            this.maxQueueSize = value;
            return this;
        }

        public Setter withQueueType(String value) {
            this.queueType = value;
            return this;
        }

        public Setter withFair(boolean value) {
            this.fair = value;
            return this;
        }

        // TODO 这里后续要重新设计拒绝策略实现方式
        public Setter withRejectedExecutionType(String value) {
            this.rejectedExecutionType = value;
            return this;
        }

        public Setter withQueueSizeRejectionThreshold(int value) {
            this.queueSizeRejectionThreshold = value;
            return this;
        }

    }
}
