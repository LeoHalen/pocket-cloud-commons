package site.halenspace.pocketcloud.threadpool;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import site.halenspace.pocketcloud.threadpool.consts.QueueTypeConst;
import site.halenspace.pocketcloud.threadpool.properties.ThreadPoolProperties;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 动态线程池工厂
 *
 * @Author Halen.Leo · 2021/6/30
 * @Blogger 后起小生
 * @Github https://github.com/LeoHalen
 */
@Slf4j
public class DynamicThreadPoolFactory {

    /**
     * 线程池名称容器
     */
    private static final Set<String> THREAD_POOL_NAME_SET = new HashSet<>();

    /**
     * 创建线程池
     * @param properties
     * @return
     */
    public DynamicThreadPoolExecutor createThreadPool(ThreadPoolProperties properties) {
        String threadPoolName = properties.getThreadPoolName();

        synchronized (THREAD_POOL_NAME_SET) {
            if (THREAD_POOL_NAME_SET.contains(threadPoolName)){
                // 如果存在
                threadPoolName += THREAD_POOL_NAME_SET.size();
            }
            THREAD_POOL_NAME_SET.add(threadPoolName);
        }

        DynamicThreadPoolExecutor dynamicThreadPoolExecutor = new DynamicThreadPoolExecutor(
                properties.getCorePoolSize(),
                properties.getMaximumPoolSize(),
                properties.getKeepAliveTime(),
                properties.getTimeUnit(),
                buildQueue(properties.getQueueType(), properties.getQueueCapacity(), properties.isFair()),
                buildThreadFactory(threadPoolName),
                properties.getRejectedExecutionType());
        log.info("Dynamic thread pool factory: initialize {}(" +
                "corePoolSize={},maximumPoolSize={},keepAliveTime={},timeUnit={},queue={},rejectedExecution={})",
                threadPoolName, properties.getCorePoolSize(), properties.getMaximumPoolSize(), properties.getKeepAliveTime(),
                properties.getTimeUnit(), properties.getQueueType(), properties.getRejectedExecutionType());
        return dynamicThreadPoolExecutor;
    }

    public ThreadFactory buildThreadFactory(String threadPoolNamePrefix) {
        return ThreadFactoryBuilder.create()
                .setNamePrefix(threadPoolNamePrefix + "-")
                .setDaemon(true).build();
    }

    private BlockingQueue<Runnable> buildQueue(String queueType, int queueCapacity, boolean fair) {

        switch (queueType) {
            case QueueTypeConst.ArrayBlockingQueue:
                return new ArrayBlockingQueue<>(queueCapacity);
            case QueueTypeConst.LinkedBlockingQueue:
                return new LinkedBlockingQueue<>(queueCapacity);
            case QueueTypeConst.LinkedBlockingDeque:
                return new LinkedBlockingDeque<>(queueCapacity);
            case QueueTypeConst.DelayQueue:
                return new DelayQueue();
            case QueueTypeConst.SynchronousQueue:
                return new SynchronousQueue<>(fair);
            case QueueTypeConst.PriorityBlockingQueue:
                return new PriorityBlockingQueue<>();
        }

        return new ArrayBlockingQueue<>(queueCapacity);
    }
}
