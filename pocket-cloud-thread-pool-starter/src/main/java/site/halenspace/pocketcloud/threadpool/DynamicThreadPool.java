package site.halenspace.pocketcloud.threadpool;

import lombok.extern.slf4j.Slf4j;
import site.halenspace.pocketcloud.threadpool.builder.RejectedExecutionBuilder;
import site.halenspace.pocketcloud.threadpool.queue.ResizableLinkedBlockingQueue;
import site.halenspace.pocketcloud.threadpool.strategy.properties.DynamicThreadPoolProperties;
import site.halenspace.pocketcloud.threadpool.strategy.properties.factory.DynamicThreadPoolPropertiesFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;

/**
 * @author Halen.Leo · 2021/9/6
 */
public interface DynamicThreadPool {

    /**
     * Implementation of {@link DynamicThreadPoolExecutor}.
     *
     * @return DynamicThreadPoolExecutor
     */
    ExecutorService getExecutor();

    /**
     * Refresh config for {@link DynamicThreadPoolExecutor}.
     */
    void refreshExecutor();


    @Slf4j
    class Factory {
        final static ConcurrentHashMap<String, DynamicThreadPool> dynamicThreadPools = new ConcurrentHashMap<>();

        public static DynamicThreadPool getInstance(DynamicThreadPoolKey threadPoolKey, DynamicThreadPoolProperties.Setter builder) {
            return getInstance(threadPoolKey, builder, null);
        }

        public static DynamicThreadPool getInstance(
                DynamicThreadPoolKey threadPoolKey, DynamicThreadPoolProperties.Setter builder, ExecutorListener listener) {
            String threadPoolName = threadPoolKey.name();
            DynamicThreadPool previouslyCached = dynamicThreadPools.get(threadPoolName);
            if (previouslyCached != null) {
                return previouslyCached;
            }
            // Cached
            dynamicThreadPools.putIfAbsent(threadPoolName, new DynamicThreadPoolDefault(threadPoolKey, builder, listener));
            return dynamicThreadPools.get(threadPoolName);
        }

        public static DynamicThreadPool getInstance(DynamicThreadPoolKey threadPoolKey) {
            String threadPoolName = threadPoolKey.name();
            DynamicThreadPool previouslyCached = dynamicThreadPools.get(threadPoolName);
            if (previouslyCached != null) {
                return previouslyCached;
            }

            if (log.isDebugEnabled()) {
                log.debug("Dynamic thread pool key for: {} does not get an 'DynamicThreadPool' instance", threadPoolKey.name());
            }
            return null;
        }
    }

    @Slf4j
    class DynamicThreadPoolDefault implements DynamicThreadPool {

        private final DynamicThreadPoolKey threadPoolKey;
        private final DynamicThreadPoolProperties properties;
        private final DynamicThreadPoolExecutor threadPool;
        private final BlockingQueue<Runnable> workingQueue;
        private final int queueSize;

        public DynamicThreadPoolDefault(DynamicThreadPoolKey threadPoolKey, DynamicThreadPoolProperties.Setter builder) {
            this(threadPoolKey, builder, null);
        }

        public DynamicThreadPoolDefault(DynamicThreadPoolKey threadPoolKey, DynamicThreadPoolProperties.Setter builder, ExecutorListener listener) {
            this.threadPoolKey = threadPoolKey;
            this.properties = DynamicThreadPoolPropertiesFactory.getThreadPoolProperties(threadPoolKey, builder);
            this.threadPool = DynamicThreadPoolFactory.getInstance().getThreadPool(threadPoolKey, this.properties);
            this.threadPool.addListener(listener);
            this.preheatAllCoreThreads();
            this.workingQueue = this.threadPool.getQueue();
            this.queueSize = this.properties.getMaxQueueSize().get();
        }

        public void preheatAllCoreThreads() {
            if (!this.properties.getPreheatEnabled().get()) {
                return;
            }
            if (log.isDebugEnabled()) {
                log.debug("Dynamic thread pool preheat for: {} preheat all core threads", threadPoolKey.name());
            }
            this.threadPool.prestartAllCoreThreads();
        }

        @Override
        public ExecutorService getExecutor() {
            refreshExecutor();
            return threadPool;
        }

        @Override
        public void refreshExecutor() {

            final int dynamicCorePoolSize = properties.getCorePoolSize().get();
            int dynamicMaximumPoolSize = properties.getMaximumPoolSize().get();
            final long dynamicKeepAliveTime = properties.getKeepAliveTime().get();
            final int dynamicMaxQueueSize = properties.getMaxQueueSize().get();
            final boolean dynamicFair = properties.getFair().get();
            final RejectedExecutionHandler rejectedExecutionHandler = RejectedExecutionBuilder.build(properties.getRejectedExecutionType().get());
            boolean maxTooLow = false;

            if (dynamicMaximumPoolSize < dynamicCorePoolSize) {
                dynamicMaximumPoolSize = dynamicCorePoolSize;
                maxTooLow = true;
            }

            // In JDK 6, setCorePoolSize and setMaximumPoolSize will execute a lock operation. Avoid them if the pool size is not changed.
            if (threadPool.getCorePoolSize() != dynamicCorePoolSize || threadPool.getMaximumPoolSize() != dynamicMaximumPoolSize) {
                if (maxTooLow) {
                    log.error("Dynamic thread pool configuration at refresh executor for: " +
                                    "{} is trying to set coreSize = {} and maximumSize = {}. MaximumSize will be set to {}, " +
                                    "the coreSize value, since it must be equal to or greater than the coreSize value",
                            threadPoolKey.name(), dynamicCorePoolSize, dynamicMaximumPoolSize, dynamicCorePoolSize);
                }
                threadPool.setCorePoolSize(dynamicCorePoolSize);
                threadPool.setMaximumPoolSize(dynamicMaximumPoolSize);
            }

            threadPool.setKeepAliveTime(dynamicKeepAliveTime, properties.getDefaultTimeUnit());
            if (threadPool.getQueue() instanceof ResizableLinkedBlockingQueue && dynamicMaxQueueSize > 0) {
                ((ResizableLinkedBlockingQueue<Runnable>) threadPool.getQueue()).setCapacity(dynamicMaxQueueSize);
            }
            threadPool.setRejectedExecutionHandler(rejectedExecutionHandler);
            // TODO 后期想办法看是否可以更换队列类型
            // TODO 后续考虑是否将SynchronousQueue公平属性支持动态修改
//            if (threadPool.getQueue() instanceof SynchronousQueue) {
//                ((SynchronousQueue<Runnable>) threadPool.getQueue()).
//            }
        }
    }
}
