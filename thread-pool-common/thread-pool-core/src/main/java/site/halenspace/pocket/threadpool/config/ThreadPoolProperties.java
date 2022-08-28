package site.halenspace.pocket.threadpool.config;

import lombok.Getter;
import site.halenspace.pocket.threadpool.ThreadPoolKey;
import site.halenspace.pocket.threadpool.config.property.Property;

import java.util.concurrent.TimeUnit;

/**
 * @author Halen Leo · 2022/8/28
 * @blogger 后起小生
 * @github <a href="https://github.com/LeoHalen"/>
 */
@Getter
public class ThreadPoolProperties {

    private final Property<Integer> corePoolSize;
    private final Property<Integer> maximumPoolSize;
    private final Property<Long> keepAliveTime;

    public ThreadPoolProperties(ThreadPoolKey key) {
        this("dynamic", key, ThreadPoolProperties.Setter());
    }

    public ThreadPoolProperties(ThreadPoolKey key, Setter setter) {
        this("dynamic", key, null != setter ? setter : ThreadPoolProperties.Setter());
    }

    public ThreadPoolProperties(String propsPrefix, ThreadPoolKey key, Setter override) {
        this.corePoolSize = getProperty(propsPrefix, key, ThreadPoolConfigDef.CORE_POOL_SIZE, override.getCorePoolSize(), ThreadPoolConfigDef.DEFAULT_CORE_POOL_SIZE);
        this.maximumPoolSize = getProperty(propsPrefix, key, ThreadPoolConfigDef.MAXIMUM_POOL_SIZE, override.getMaximumPoolSize(), ThreadPoolConfigDef.DEFAULT_MAXIMUM_POOL_SIZE);
        this.keepAliveTime = getProperty(propsPrefix, key, ThreadPoolConfigDef.KEEP_ALIVE_TIME, override.getKeepAliveTime(), ThreadPoolConfigDef.DEFAULT_KEEP_ALIVE_TIME);
    }

    private static Property<Integer> getProperty(String propertyPrefix, ThreadPoolKey key, String propName, Integer overrideValue, Integer defaultValue) {
        return ThreadPoolPropertiesChainedProperty.forInteger()
                .with(propertyPrefix + ".thread-pool." + key.get() + "." + propName, overrideValue)
                .with(propertyPrefix + ".thread-pool.default." + propName, defaultValue)
                .build();
    }

    private static Property<Boolean> getProperty(String propertyPrefix, ThreadPoolKey key, String instanceProperty, Boolean overrideValue, Boolean defaultValue) {
        return ThreadPoolPropertiesChainedProperty.forBoolean()
                .with(propertyPrefix + ".thread-pool." + key.get() + "." + instanceProperty, overrideValue)
                .with(propertyPrefix + ".thread-pool.default." + instanceProperty, defaultValue)
                .build();
    }

    private static Property<Long> getProperty(String propertyPrefix, ThreadPoolKey key, String instanceProperty, Long overrideValue, Long defaultValue) {
        return ThreadPoolPropertiesChainedProperty.forLong()
                .with(propertyPrefix + ".thread-pool." + key.get() + "." + instanceProperty, overrideValue)
                .with(propertyPrefix + ".thread-pool.default." + instanceProperty, defaultValue)
                .build();
    }

    private static Property<Float> getProperty(String propertyPrefix, ThreadPoolKey key, String instanceProperty, Float overrideValue, Float defaultValue) {
        return ThreadPoolPropertiesChainedProperty.forFloat()
                .with(propertyPrefix + ".thread-pool." + key.get() + "." + instanceProperty, overrideValue)
                .with(propertyPrefix + ".thread-pool.default." + instanceProperty, defaultValue)
                .build();
    }

    private static Property<String> getProperty(String propertyPrefix, ThreadPoolKey key, String instanceProperty, String overrideValue, String defaultValue) {
        return ThreadPoolPropertiesChainedProperty.forString()
                .with(propertyPrefix + ".thread-pool." + key.get() + "." + instanceProperty, overrideValue)
                .with(propertyPrefix + ".thread-pool.default." + instanceProperty, defaultValue)
                .build();
    }

    public static Setter Setter() {
        return new Setter();
    }

    @Getter
    public static class Setter {

        private Integer corePoolSize = null;
        private Integer maximumPoolSize = null;
        private Long keepAliveTime = null;
        private TimeUnit timeUnit = null;

        /* package */ Setter() {}

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
    }
}
