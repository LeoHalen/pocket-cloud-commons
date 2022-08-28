package site.halenspace.pocket.threadpool;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * @author Halen Leo · 2022/8/28
 * @blogger 后起小生
 * @github <a href="https://github.com/LeoHalen"/>
 */
@Slf4j
public class ThreadPoolHandler {

    private final static ConcurrentHashMap<String, ThreadPoolManager> managerCached = new ConcurrentHashMap<>();

    /**
     * Get a {@link ThreadPoolManager} instance by thread pool name, create one if it exists.
     * @param threadPoolName thread pool name, never <code>null</code>
     * @return never <code>null</code>
     */
    public static ThreadPoolManager getManager(@Nonnull String threadPoolName) {
        Preconditions.checkNotNull(threadPoolName);
        if (managerCached.containsKey(threadPoolName)) {
            return managerCached.get(threadPoolName);
        }

        ThreadPoolKey threadPoolKey = ThreadPoolKey.Factory.asKey(threadPoolName);
        managerCached.putIfAbsent(threadPoolName, new ThreadPoolManager(threadPoolKey));

        return managerCached.get(threadPoolName);
    }

    /**
     * Get a {@link ThreadPoolManager} instance by {@link ThreadPoolKey}, create one if it exists.
     * @param threadPoolKey thread pool key, never <code>null</code>
     * @return never <code>null</code>
     */
    public static ThreadPoolManager getManager(@Nonnull ThreadPoolKey threadPoolKey) {
        Preconditions.checkNotNull(threadPoolKey);
        return getManager(threadPoolKey.get());
    }

    /**
     * Get a {@link ExecutorService} implements {@link DynamicThreadPoolExecutor} instance
     * by thread pool name, create one if it exists.
     * @param threadPoolName thread pool name, never <code>null</code>
     * @return never <code>null</code>
     */
    public static ExecutorService getExecutor(@Nonnull String threadPoolName) {
        return getManager(threadPoolName).getExecutor();
    }

    /**
     * Get a {@link ExecutorService} implements {@link DynamicThreadPoolExecutor} instance
     * by {@link ThreadPoolKey}, create one if it exists.
     * @param threadPoolKey thread pool key, never <code>null</code>
     * @return never <code>null</code>
     */
    public static ExecutorService getExecutor(@Nonnull ThreadPoolKey threadPoolKey) {
        return getManager(threadPoolKey).getExecutor();
    }

    /**
     * Delegate {@link ThreadPoolManager} refreshes configuration updates to the {@link DynamicThreadPoolExecutor}.
     * @param threadPoolName thread pool name, never <code>null</code>
     */
    public static void refreshExecutor(@Nonnull String threadPoolName) {
        Preconditions.checkNotNull(threadPoolName);
        if (!managerCached.containsKey(threadPoolName)) {
            log.warn("The thread pool '{}' not existed.", threadPoolName);
            return;
        }
        managerCached.get(threadPoolName).refreshExecutor();
    }

    /**
     * Delegate {@link ThreadPoolManager} refreshes configuration updates to the {@link DynamicThreadPoolExecutor}.
     * @param threadPoolKey thread pool key, never <code>null</code>
     */
    public static void refreshExecutor(@Nonnull ThreadPoolKey threadPoolKey) {
        Preconditions.checkNotNull(threadPoolKey);
        refreshExecutor(threadPoolKey.get());
    }
}
