package site.halenspace.pocketcloud.threadpool.strategy.properties;

import site.halenspace.pocketcloud.threadpool.DynamicThreadPoolKey;

/**
 * @author Halen Leo · 2021/9/5
 */
public abstract class ThreadPoolPropertiesStrategy {


    /**
     * Construct an implementation of {@link DynamicThreadPoolProperties} for {@link site.halenspace.pocketcloud.threadpool.DynamicThreadPool} instances with {@link DynamicThreadPoolKey}.
     * <p>
     * <b>Default Implementation</b>
     * <p>
     * Constructs instance of {@link DynamicThreadPoolPropertiesDefault}.
     *
     * @param threadPoolKey
     *            {@link DynamicThreadPoolKey} representing the name or type of {@link site.halenspace.pocketcloud.threadpool.DynamicThreadPool}
     * @param builder
     *            {@link DynamicThreadPoolProperties.Setter} with default overrides as injected via {@link } to the {@link site.halenspace.pocketcloud.threadpool.DynamicThreadPool} implementation.
     *            <p>
     *            The builder will return NULL for each value if no override was provided.
     *
     * @return Implementation of {@link DynamicThreadPoolProperties}
     */
    public DynamicThreadPoolProperties getThreadPoolProperties(DynamicThreadPoolKey threadPoolKey, DynamicThreadPoolProperties.Setter builder) {
        return new DynamicThreadPoolPropertiesDefault(threadPoolKey, builder);
    }

    /**
     * Cache key used for caching the retrieval of {@link DynamicThreadPoolProperties} implementations.
     * <p>
     * Typically this would return <code>DynamicThreadPoolKey.name()</code> but can be done differently if required.
     * <p>
     * For example, null can be returned which would cause it to not cache and invoke {@link #getThreadPoolProperties} for each {@link site.halenspace.pocketcloud.threadpool.DynamicThreadPool} instantiation (not recommended).
     * <p>
     * <b>Default Implementation</b>
     * <p>
     * Returns {@link DynamicThreadPoolKey#name()}
     *
     * @param threadPoolKey thread pool key used in determining thread pool's cache key
     * @param builder builder for {@link DynamicThreadPoolProperties} used in determining thread pool's cache key
     * @return String value to be used as the cache key of a {@link DynamicThreadPoolProperties} implementation.
     */
    public String getThreadPoolPropertiesCacheKey(DynamicThreadPoolKey threadPoolKey, DynamicThreadPoolProperties.Setter builder) {
        return threadPoolKey.name();
    }


//    /**
//     * Construct an implementation of {@link com.netflix.hystrix.HystrixTimerThreadPoolProperties} for configuration of the timer thread pool
//     * that handles timeouts and collapser logic.
//     * <p>
//     * Constructs instance of {@link HystrixPropertiesTimerThreadPoolDefault}.
//     *
//     *
//     * @return Implementation of {@link com.netflix.hystrix.HystrixTimerThreadPoolProperties}
//     */
//    public HystrixTimerThreadPoolProperties getTimerThreadPoolProperties() {
//        return new HystrixPropertiesTimerThreadPoolDefault();
//    }

}
