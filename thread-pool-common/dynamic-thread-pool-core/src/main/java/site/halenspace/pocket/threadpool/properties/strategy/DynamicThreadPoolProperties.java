package site.halenspace.pocket.threadpool.properties.strategy;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import site.halenspace.pocket.threadpool.DynamicThreadPoolKey;
import site.halenspace.pocket.threadpool.consts.QueueTypeConst;
import site.halenspace.pocket.threadpool.consts.RejectedExecutionHandlerTypeConst;
import site.halenspace.pocket.threadpool.properties.ThreadPoolPropertiesChainedProperty;
import site.halenspace.pocket.threadpool.properties.property.Property;

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
public class DynamicThreadPoolProperties {

    /**
     * 线程池名称, 默认值为 "dynamic-thread-pool"
     */
    private String defaultThreadPoolName = "dynamic-thread-pool";

    /**
     * 核心线程数, 默认值为CPU核心数量
     */
    private final int defaultCorePoolSize = Runtime.getRuntime().availableProcessors();

    /**
     * 最大线程数, 默认值为(2 * CPU核心数量)
     */
    private final int defaultMaximumPoolSize = 2 * Runtime.getRuntime().availableProcessors();

    /**
     * 空闲线程存活时间
     */
    private final long defaultKeepAliveTime = 60;

    /**
     * 空闲线程存活时间单位, 默认为秒
     * @See TimeUnit
     */
    private final TimeUnit defaultTimeUnit = TimeUnit.SECONDS;

    /**
     * 队列最大数量
     */
    private final int defaultMaxQueueSize = Integer.MAX_VALUE;

    /**
     * 队列类型
     * @see QueueTypeConst
     */
    private final String defaultQueueType = QueueTypeConst.LinkedBlockingQueue;

    /**
     * SynchronousQueue 是否公平策略
     */
    private final boolean defaultFair = Boolean.FALSE;

    /**
     * 拒绝策略, 默认丢弃任务并抛出异常
     * @see RejectedExecutionHandlerTypeConst
     */
    private final String defaultRejectedExecutionType = RejectedExecutionHandlerTypeConst.AbortPolicy;

    /**
     * 队列负载因子, 默认为最大值1
     */
    private final float defaultQueueWarningLoadFactor = 1F;

    /**
     * 队列负载阈值阀门打开分钟级滚动窗口, 默认为5分钟
     */
    private final long defaultQueueThresholdValveOpenRollingWindowInMinutes = 5;

    /**
     * 队列容量告警阀值, 默认为队列容量
     */
//    private final int defaultQueueSizeWarningThreshold = (int) (defaultMaxQueueSize * defaultQueueWarningLoadFactor);

    /**
     * 预热是否打开, 默认关闭
     */
    private final boolean defaultPreheatEnabled = Boolean.FALSE;

    /**
     * 懒加载模式开关, 默认打开
     * @Describe 懒加载模式打开后，线程池执行器的初始化时机为第一次获取时
     */
    private final boolean lazyModeEnabled = Boolean.TRUE;


    private final Property<Integer> corePoolSize;
    private final Property<Integer> maximumPoolSize;
    private final Property<Long> keepAliveTime;
    private final Property<String> queueType;
    private final Property<Integer> maxQueueSize;
    private final Property<Boolean> fair;
    private final Property<String> rejectedExecutionType;
    private final Property<Float> queueWarningLoadFactor;
    private final Property<Long> queueThresholdValveOpenRollingWindowInMinutes;
    private final Property<Boolean> preheatEnabled;
//    private final Property<Boolean> allowMaximumSizeToDivergeFromCoreSize;
//    private final Property<Integer> threadPoolRollingNumberStatisticalWindowInMilliseconds;
//    private final Property<Integer> threadPoolRollingNumberStatisticalWindowBuckets;


    protected DynamicThreadPoolProperties(DynamicThreadPoolKey key) {
        this(key, new Setter(), "dynamic");
    }

    protected DynamicThreadPoolProperties(DynamicThreadPoolKey key, Setter builder) {
        this(key, builder, "dynamic");
    }

    protected DynamicThreadPoolProperties(DynamicThreadPoolKey key, Setter builder, String propertyPrefix) {
        this.corePoolSize = getProperty(propertyPrefix, key, "corePoolSize", builder.getCorePoolSize(), defaultCorePoolSize);
        this.maximumPoolSize = getProperty(propertyPrefix, key, "maximumPoolSize", builder.getMaximumPoolSize(), defaultMaximumPoolSize);
        this.keepAliveTime = getProperty(propertyPrefix, key, "keepAliveTime", builder.getKeepAliveTime(), defaultKeepAliveTime);
        this.queueType = getProperty(propertyPrefix, key, "queueType", builder.getQueueType(), defaultQueueType);
        this.maxQueueSize = getProperty(propertyPrefix, key, "maxQueueSize", builder.getMaxQueueSize(), defaultMaxQueueSize);
        this.fair = getProperty(propertyPrefix, key, "fair", builder.getFair(), defaultFair);
        Property<Float> queueWarningLoadFactorProperty = getProperty(propertyPrefix, key, "queueWarningLoadFactor", builder.getQueueWarningLoadFactor(), defaultQueueWarningLoadFactor);
        this.queueWarningLoadFactor = queueWarningLoadFactorProperty.get() > defaultQueueWarningLoadFactor ?
                getProperty(propertyPrefix, key, "queueWarningLoadFactor", defaultQueueWarningLoadFactor, defaultQueueWarningLoadFactor) : queueWarningLoadFactorProperty;
        this.rejectedExecutionType = getProperty(propertyPrefix, key, "rejectedExecutionType", builder.getRejectedExecutionType(), defaultRejectedExecutionType);
        this.queueThresholdValveOpenRollingWindowInMinutes = getProperty(propertyPrefix, key, "queueThresholdValveOpenRollingWindowInMinutes", builder.getQueueThresholdValveOpenRollingWindowInMinutes(), defaultQueueThresholdValveOpenRollingWindowInMinutes);
        this.preheatEnabled = getProperty(propertyPrefix, key, "preheatEnabled", builder.getPreheatEnabled(), defaultPreheatEnabled);
    }

    private static Property<Integer> getProperty(String propertyPrefix, DynamicThreadPoolKey key, String instanceProperty, Integer builderOverrideValue, Integer defaultValue) {
        return ThreadPoolPropertiesChainedProperty.forInteger()
                .add(propertyPrefix + ".threadpool." + key.name() + "." + instanceProperty, builderOverrideValue)
                .add(propertyPrefix + ".threadpool.default." + instanceProperty, defaultValue)
                .build();
    }

    private static Property<Boolean> getProperty(String propertyPrefix, DynamicThreadPoolKey key, String instanceProperty, Boolean builderOverrideValue, Boolean defaultValue) {
        return ThreadPoolPropertiesChainedProperty.forBoolean()
                .add(propertyPrefix + ".threadpool." + key.name() + "." + instanceProperty, builderOverrideValue)
                .add(propertyPrefix + ".threadpool.default." + instanceProperty, defaultValue)
                .build();
    }

    private static Property<Long> getProperty(String propertyPrefix, DynamicThreadPoolKey key, String instanceProperty, Long builderOverrideValue, Long defaultValue) {
        return ThreadPoolPropertiesChainedProperty.forLong()
                .add(propertyPrefix + ".threadpool." + key.name() + "." + instanceProperty, builderOverrideValue)
                .add(propertyPrefix + ".threadpool.default." + instanceProperty, defaultValue)
                .build();
    }

    private static Property<Float> getProperty(String propertyPrefix, DynamicThreadPoolKey key, String instanceProperty, Float builderOverrideValue, Float defaultValue) {
        return ThreadPoolPropertiesChainedProperty.forFloat()
                .add(propertyPrefix + ".threadpool." + key.name() + "." + instanceProperty, builderOverrideValue)
                .add(propertyPrefix + ".threadpool.default." + instanceProperty, defaultValue)
                .build();
    }

    private static Property<String> getProperty(String propertyPrefix, DynamicThreadPoolKey key, String instanceProperty, String builderOverrideValue, String defaultValue) {
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
        private Float queueWarningLoadFactor = null;
        private Long queueThresholdValveOpenRollingWindowInMinutes = null;
        private Boolean preheatEnabled = null;

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

        public Setter withQueueWarningLoadFactor(float value) {
            this.queueWarningLoadFactor = value;
            return this;
        }

        public Setter withQueueThresholdValveOpenRollingWindowInMinutes(long value) {
            this.queueThresholdValveOpenRollingWindowInMinutes = value;
            return this;
        }

        public Setter withPreheatEnabled(boolean value) {
            this.preheatEnabled = value;
            return this;
        }
    }
}
