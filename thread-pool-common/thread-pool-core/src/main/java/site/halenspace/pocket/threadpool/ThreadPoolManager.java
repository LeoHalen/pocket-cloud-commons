package site.halenspace.pocket.threadpool;

import lombok.extern.slf4j.Slf4j;
import site.halenspace.pocket.threadpool.config.ThreadPoolConfigDef;
import site.halenspace.pocket.threadpool.config.ThreadPoolProperties;
import site.halenspace.pocket.threadpool.factory.ThreadPoolExecutorFactory;

import java.util.concurrent.ExecutorService;

/**
 * @author Halen Leo · 2022/8/28
 * @blogger 后起小生
 * @github <a href="https://github.com/LeoHalen"/>
 */
@Slf4j
public class ThreadPoolManager {

    private final ThreadPoolKey threadPoolKey;

    private final ThreadPoolProperties properties;

    private final DynamicThreadPoolExecutor executor;


    public ThreadPoolManager(ThreadPoolKey threadPoolKey) {
        this(threadPoolKey, null);
    }

    public ThreadPoolManager(ThreadPoolKey threadPoolKey, ThreadPoolProperties.Setter setter) {
        this.threadPoolKey = threadPoolKey;
        this.properties = new ThreadPoolProperties(threadPoolKey, setter);
        this.executor = ThreadPoolExecutorFactory.getExecutor(threadPoolKey, properties);
    }

    public ThreadPoolKey getThreadPoolKey() {
        return threadPoolKey;
    }

    public ThreadPoolProperties getProperties() {
        return properties;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public void refreshExecutor() {
        String threadPoolName = threadPoolKey.get();
        final int corePoolSize = properties.getCorePoolSize().get();
        int maximumPoolSize = properties.getMaximumPoolSize().get();
        final long keepAliveTime = properties.getKeepAliveTime().get();

        boolean maxTooLow = false;
        if (maximumPoolSize < corePoolSize) {
            maximumPoolSize = corePoolSize;
            maxTooLow = true;
        }

        if (executor.getCorePoolSize() != corePoolSize || executor.getMaximumPoolSize() != maximumPoolSize) {
            if (maxTooLow) {
                log.error("Dynamic thread pool configuration at refresh executor for: " +
                                "{} is trying to set coreSize = {} and maximumSize = {}. MaximumSize will be set to {}, " +
                                "the coreSize value, since it must be equal to or greater than the coreSize value",
                        threadPoolName, corePoolSize, maximumPoolSize, corePoolSize);
            }
            executor.setCorePoolSize(corePoolSize);
            executor.setMaximumPoolSize(maximumPoolSize);
        }

        if (executor.getKeepAliveTime(ThreadPoolConfigDef.DEFAULT_TIMEUNIT) != keepAliveTime) {
            executor.setKeepAliveTime(keepAliveTime, ThreadPoolConfigDef.DEFAULT_TIMEUNIT);
        }

        if (log.isDebugEnabled()) {
            log.debug("Dynamic thread pool executor refresh for: " +
                            "{}(corePoolSize={}, maximumPoolSize={}, keepAliveTime={}, timeUnit={})",
                    threadPoolName, corePoolSize, corePoolSize, keepAliveTime, ThreadPoolConfigDef.DEFAULT_TIMEUNIT);
        }
    }
}
