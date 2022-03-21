package site.halenspace.pocket.threadpool;

import lombok.extern.slf4j.Slf4j;
import site.halenspace.pocket.threadpool.builder.RejectedExecutionBuilder;
import site.halenspace.pocket.threadpool.properties.strategy.DynamicThreadPoolProperties;
import site.halenspace.pocket.threadpool.properties.factory.DynamicThreadPoolPropertiesFactory;
import site.halenspace.pocket.threadpool.queue.ResizableLinkedBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;

/**
 * @author Halen.Leo · 2021/9/6
 */
public interface DynamicThreadPool {

    /**
     * Implementation of {@link DynamicThreadPoolDefault}.
     *
     * @return ExecutorService
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
        private final DynamicThreadPoolExecutor threadPoolExecutor;
        private final BlockingQueue<Runnable> workingQueue;
        private final int queueSize;

        public DynamicThreadPoolDefault(DynamicThreadPoolKey threadPoolKey, DynamicThreadPoolProperties.Setter builder) {
            this(threadPoolKey, builder, null);
        }

        public DynamicThreadPoolDefault(DynamicThreadPoolKey threadPoolKey, DynamicThreadPoolProperties.Setter builder, ExecutorListener listener) {
            this.threadPoolKey = threadPoolKey;
            this.properties = DynamicThreadPoolPropertiesFactory.getThreadPoolProperties(threadPoolKey, builder);
            this.threadPoolExecutor = DynamicThreadPoolFactory.getInstance().getThreadPoolExecutor(threadPoolKey, this.properties, listener);
            this.workingQueue = this.threadPoolExecutor.getQueue();
            this.queueSize = this.properties.getMaxQueueSize().get();
        }

        @Override
        public ExecutorService getExecutor() {
            refreshExecutor();
            return threadPoolExecutor;
        }

        @Override
        public void refreshExecutor() {

            final int dynamicCorePoolSize = properties.getCorePoolSize().get();
            int dynamicMaximumPoolSize = properties.getMaximumPoolSize().get();
            final long dynamicKeepAliveTime = properties.getKeepAliveTime().get();
            final int dynamicMaxQueueSize = properties.getMaxQueueSize().get();
            final float dynamicQueueWarningLoadFactor = properties.getQueueWarningLoadFactor().get();
            final int dynamicQueueSizeWarningThreshold = (int) (dynamicMaxQueueSize * dynamicQueueWarningLoadFactor);
            final boolean dynamicFair = properties.getFair().get();
            final RejectedExecutionHandler rejectedExecutionHandler = RejectedExecutionBuilder.build(properties.getRejectedExecutionType().get());
            boolean maxTooLow = false;

            if (dynamicMaximumPoolSize < dynamicCorePoolSize) {
                dynamicMaximumPoolSize = dynamicCorePoolSize;
                maxTooLow = true;
            }

            // In JDK 6, setCorePoolSize and setMaximumPoolSize will execute a lock operation. Avoid them if the pool size is not changed.
            if (threadPoolExecutor.getCorePoolSize() != dynamicCorePoolSize || threadPoolExecutor.getMaximumPoolSize() != dynamicMaximumPoolSize) {
                if (maxTooLow) {
                    log.error("Dynamic thread pool configuration at refresh executor for: " +
                                    "{} is trying to set coreSize = {} and maximumSize = {}. MaximumSize will be set to {}, " +
                                    "the coreSize value, since it must be equal to or greater than the coreSize value",
                            threadPoolKey.name(), dynamicCorePoolSize, dynamicMaximumPoolSize, dynamicCorePoolSize);
                }
                threadPoolExecutor.setCorePoolSize(dynamicCorePoolSize);
                threadPoolExecutor.setMaximumPoolSize(dynamicMaximumPoolSize);
            }

            threadPoolExecutor.setKeepAliveTime(dynamicKeepAliveTime, properties.getDefaultTimeUnit());
            if (threadPoolExecutor.getQueue() instanceof ResizableLinkedBlockingQueue && dynamicMaxQueueSize > 0) {
                ((ResizableLinkedBlockingQueue<Runnable>) threadPoolExecutor.getQueue()).setCapacity(dynamicMaxQueueSize);
            }
            threadPoolExecutor.setBoundedQueueLoadFactor(dynamicQueueWarningLoadFactor);
            threadPoolExecutor.setBoundedQueueSizeWarningThreshold(dynamicQueueSizeWarningThreshold);
            threadPoolExecutor.setRejectedExecutionHandler(rejectedExecutionHandler);
            // TODO 后期想办法看是否可以更换队列类型
            // TODO 后续考虑是否将SynchronousQueue公平属性支持动态修改
//            if (threadPool.getQueue() instanceof SynchronousQueue) {
//                ((SynchronousQueue<Runnable>) threadPool.getQueue()).
//            }
        }
    }
}
