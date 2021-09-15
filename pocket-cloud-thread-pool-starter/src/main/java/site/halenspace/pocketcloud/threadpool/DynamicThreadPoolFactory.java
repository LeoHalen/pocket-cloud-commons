package site.halenspace.pocketcloud.threadpool;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import site.halenspace.pocketcloud.threadpool.builder.ExecutorBlockingQueueBuilder;
import site.halenspace.pocketcloud.threadpool.builder.RejectedExecutionBuilder;
import site.halenspace.pocketcloud.threadpool.consts.QueueTypeConst;
import site.halenspace.pocketcloud.threadpool.strategy.properties.DynamicThreadPoolProperties;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 动态线程池工厂类
 *
 * @author Halen Leo · 2021/6/30
 * @blogger 后起小生
 * @github https://github.com/LeoHalen
 */
@Slf4j
public class DynamicThreadPoolFactory implements
        ThreadPoolFactory<DynamicThreadPoolExecutor, DynamicThreadPoolKey, DynamicThreadPoolProperties> {

    private static class LazyHolder {
        private static final DynamicThreadPoolFactory INSTANCE = new DynamicThreadPoolFactory();
    }

    public static DynamicThreadPoolFactory getInstance() {
        return DynamicThreadPoolFactory.LazyHolder.INSTANCE;
    }

    @Override
    public DynamicThreadPoolExecutor getThreadPoolExecutor(DynamicThreadPoolKey threadPoolKey, DynamicThreadPoolProperties properties) {
        return getThreadPoolExecutor(threadPoolKey, properties, null);
    }

    @Override
    public DynamicThreadPoolExecutor getThreadPoolExecutor(
            final DynamicThreadPoolKey threadPoolKey, DynamicThreadPoolProperties properties, ExecutorListener listener) {

        final String threadPoolName = threadPoolKey.name();
        final int dynamicCorePoolSize = properties.getCorePoolSize().get();
        final int dynamicMaximumPoolSize = properties.getMaximumPoolSize().get();
        final long dynamicKeepAliveTime = properties.getKeepAliveTime().get();
        final TimeUnit defaultTimeUnit = properties.getDefaultTimeUnit();
        final BlockingQueue<Runnable> workingQueue = getBlockingQueue(properties);
        final String queType = properties.getQueueType().get();
        final int queueSize = properties.getMaxQueueSize().get();
        final float queueWarningLoadFactor = properties.getQueueWarningLoadFactor().get();
        final ThreadFactory threadFactory = getThreadFactory(threadPoolKey);
        final RejectedExecutionHandler rejectedExecution = getRejectedExecution(properties);

        if (dynamicCorePoolSize > dynamicMaximumPoolSize) {
            log.error("Dynamic thread pool factory at initialize for: " +
                            "{} is trying to set corePoolSize = {} and maximumPoolSize = {}. MaximumPoolSize will be set to {}, " +
                            "the coreSize value, since it must be equal to or greater than the coreSize value",
                    threadPoolName, dynamicCorePoolSize, dynamicMaximumPoolSize, dynamicCorePoolSize);
            if (log.isDebugEnabled()) {
                log.debug("Dynamic thread pool factory at initialize for: " +
                                "{}(corePoolSize={},maximumPoolSize={},keepAliveTime={},timeUnit={},queue={},rejectedExecution={})",
                        threadPoolName, dynamicCorePoolSize, dynamicCorePoolSize, dynamicKeepAliveTime,
                        defaultTimeUnit, properties.getQueueType().get(), properties.getRejectedExecutionType().get());
            }
            DynamicThreadPoolExecutor threadPoolExecutor = new DynamicThreadPoolExecutor(
                    dynamicCorePoolSize, dynamicCorePoolSize, dynamicKeepAliveTime, defaultTimeUnit, workingQueue, threadFactory, rejectedExecution);
            if (QueueTypeConst.isBoundedQueue(queType)) {
                threadPoolExecutor.setBoundedQueueLoadFactor(queueWarningLoadFactor);
                threadPoolExecutor.setBoundedQueueSizeWarningThreshold((int) (queueSize * queueWarningLoadFactor));
            }
            threadPoolExecutor.addListener(listener);
            return threadPoolExecutor;
        }

        if (log.isDebugEnabled()) {
            log.debug("Dynamic thread pool factory at initialize for: " +
                            "{}(corePoolSize={},maximumPoolSize={},keepAliveTime={},timeUnit={},queue={},rejectedExecution={})",
                    threadPoolName, dynamicCorePoolSize, dynamicMaximumPoolSize, dynamicKeepAliveTime,
                    defaultTimeUnit, properties.getQueueType().get(), properties.getRejectedExecutionType().get());
        }
        DynamicThreadPoolExecutor threadPoolExecutor = new DynamicThreadPoolExecutor(
                dynamicCorePoolSize, dynamicMaximumPoolSize, dynamicKeepAliveTime, defaultTimeUnit, workingQueue, threadFactory, rejectedExecution);
        if (QueueTypeConst.isBoundedQueue(queType)) {
            threadPoolExecutor.setBoundedQueueLoadFactor(queueWarningLoadFactor);
            threadPoolExecutor.setBoundedQueueSizeWarningThreshold((int) (queueSize * queueWarningLoadFactor));
        }
        threadPoolExecutor.addListener(listener);
        return threadPoolExecutor;
    }

    public ThreadFactory getThreadFactory(final DynamicThreadPoolKey threadPoolKey) {
        return ThreadFactoryBuilder.create()
                .setNamePrefix("dynamic-" + threadPoolKey.name())
                .setDaemon(true).build();
    }

    public BlockingQueue<Runnable> getBlockingQueue(final DynamicThreadPoolProperties properties) {
        return ExecutorBlockingQueueBuilder
                .build(properties.getQueueType().get(), properties.getMaxQueueSize().get(), properties.getFair().get());
    }

    public RejectedExecutionHandler getRejectedExecution(final DynamicThreadPoolProperties properties) {
        return RejectedExecutionBuilder.build(properties.getRejectedExecutionType().get());
    }
}
