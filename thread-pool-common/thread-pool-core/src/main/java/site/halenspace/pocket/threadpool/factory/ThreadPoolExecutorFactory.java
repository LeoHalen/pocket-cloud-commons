package site.halenspace.pocket.threadpool.factory;

import lombok.extern.slf4j.Slf4j;
import site.halenspace.pocket.threadpool.DynamicThreadPoolExecutor;
import site.halenspace.pocket.threadpool.ThreadPoolKey;
import site.halenspace.pocket.threadpool.config.ThreadPoolConfigDef;
import site.halenspace.pocket.threadpool.config.ThreadPoolProperties;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Halen Leo · 2022/8/28
 * @blogger 后起小生
 * @github <a href="https://github.com/LeoHalen"/>
 */
@Slf4j
public class ThreadPoolExecutorFactory {

    public static DynamicThreadPoolExecutor getExecutor(ThreadPoolKey threadPoolKey) {
        return getExecutor(threadPoolKey, new ThreadPoolProperties(threadPoolKey));
    }

    public static DynamicThreadPoolExecutor getExecutor(ThreadPoolKey threadPoolKey, ThreadPoolProperties properties) {
        String threadPoolName = threadPoolKey.get();
        final int corePoolSize = properties.getCorePoolSize().get();
        final int maximumPoolSize = properties.getMaximumPoolSize().get();
        final long keepAliveTime = properties.getKeepAliveTime().get();
        TimeUnit timeUnit = ThreadPoolConfigDef.DEFAULT_TIMEUNIT;

        DynamicThreadPoolExecutor executor = null;
        if (corePoolSize > maximumPoolSize) {
            log.info("Dynamic thread pool factory at initialize for: " +
                            "{} is trying to set corePoolSize = {} and maximumPoolSize = {}. MaximumPoolSize will be set to {}, " +
                            "the coreSize value, since it must be equal to or greater than the coreSize value",
                    threadPoolName, corePoolSize, maximumPoolSize, corePoolSize);
            if (log.isDebugEnabled()) {
                log.debug("Dynamic thread pool factory at initialize for: " +
                                "{}(corePoolSize={}, maximumPoolSize={}, keepAliveTime={}, timeUnit={})",
                        threadPoolName, corePoolSize, corePoolSize, keepAliveTime, timeUnit);
            }
            executor = new DynamicThreadPoolExecutor(
                    corePoolSize,
                    corePoolSize,
                    keepAliveTime,
                    timeUnit,
                    new LinkedBlockingQueue<>()
            );
        }

        if (executor == null) {
            if (log.isDebugEnabled()) {
                log.debug("Dynamic thread pool factory at initialize for: " +
                                "{}(corePoolSize={}, maximumPoolSize={}, keepAliveTime={}, timeUnit={})",
                        threadPoolName, corePoolSize, maximumPoolSize, keepAliveTime, timeUnit);
            }
            executor = new DynamicThreadPoolExecutor(
                    corePoolSize,
                    maximumPoolSize,
                    keepAliveTime,
                    timeUnit,
                    new LinkedBlockingQueue<>()
            );
        }

        return executor;
    }
}
