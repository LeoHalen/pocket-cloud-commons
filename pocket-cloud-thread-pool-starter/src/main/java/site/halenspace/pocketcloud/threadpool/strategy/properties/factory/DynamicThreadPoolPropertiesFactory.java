package site.halenspace.pocketcloud.threadpool.strategy.properties.factory;

import site.halenspace.pocketcloud.threadpool.DynamicThreadPoolKey;
import site.halenspace.pocketcloud.threadpool.strategy.ThreadPoolPlugins;
import site.halenspace.pocketcloud.threadpool.strategy.properties.DynamicThreadPoolProperties;
import site.halenspace.pocketcloud.threadpool.strategy.properties.ThreadPoolPropertiesStrategy;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Halen Leo · 2021/9/6
 */
public class DynamicThreadPoolPropertiesFactory {

    // String is ThreadPoolKey.name() (we can't use ThreadPoolKey directly as we can't guarantee it implements hashcode/equals correctly)
    private static final ConcurrentHashMap<String, DynamicThreadPoolProperties> threadPoolPropertiesMap = new ConcurrentHashMap<>();

    /**
     * Get an instance of {@link DynamicThreadPoolProperties} with the given factory {@link ThreadPoolPropertiesStrategy} implementation for each {@link HystrixThreadPool} instance.
     *
     * @param key
     *            Pass-thru to {@link ThreadPoolPropertiesStrategy#getThreadPoolProperties} implementation.
     * @param builder
     *            Pass-thru to {@link ThreadPoolPropertiesStrategy#getThreadPoolProperties} implementation.
     * @return {@link DynamicThreadPoolProperties} instance
     */
    public static DynamicThreadPoolProperties getThreadPoolProperties(DynamicThreadPoolKey key, DynamicThreadPoolProperties.Setter builder) {
        ThreadPoolPropertiesStrategy threadPoolPropertiesStrategy = ThreadPoolPlugins.getInstance().getPropertiesStrategy();
        String cacheKey = threadPoolPropertiesStrategy.getThreadPoolPropertiesCacheKey(key, builder);
        if (cacheKey == null) {
            // no cacheKey so we generate it with caching
            return threadPoolPropertiesStrategy.getThreadPoolProperties(key, builder);
        }
        DynamicThreadPoolProperties properties = threadPoolPropertiesMap.get(cacheKey);
        if (properties != null) {
            return properties;
        }
        if (builder == null) {
            builder = DynamicThreadPoolProperties.Setter();
        }
        // create new instance
        properties = threadPoolPropertiesStrategy.getThreadPoolProperties(key, builder);
        // cache and return
        DynamicThreadPoolProperties existing = threadPoolPropertiesMap.putIfAbsent(cacheKey, properties);
        if (existing == null) {
            return properties;
        }
        return existing;
    }
}
