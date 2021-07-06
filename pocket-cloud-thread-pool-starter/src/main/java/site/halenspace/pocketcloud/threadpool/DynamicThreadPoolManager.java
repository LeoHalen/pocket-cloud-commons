package site.halenspace.pocketcloud.threadpool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import site.halenspace.pocketcloud.threadpool.builder.RejectedExecutionBuilder;
import site.halenspace.pocketcloud.threadpool.exeception.ExecutorCreateFailureException;
import site.halenspace.pocketcloud.threadpool.properties.ThreadPoolProperties;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 动态线程池管理器
 *
 * @Author Halen.Leo · 2021/7/2
 * @Blogger 后起小生
 * @Github https://github.com/LeoHalen
 */
@Slf4j
@Component
public class DynamicThreadPoolManager {

    /**
     * 动态线程池执行器容器
     *  ConcurrentMap.Entry => {
     *      key: ThreadPoolName,
     *      value: DynamicThreadPoolExecutor
     *  }
     */
    private static final ConcurrentMap<String, DynamicThreadPoolExecutor> executorContainer = new ConcurrentHashMap<>();

    /**
     * 线程池工厂
     */
    private static final ThreadPoolFactory<DynamicThreadPoolExecutor, ThreadPoolProperties> threadPoolFactory = new DynamicThreadPoolFactory();

    /**
     * 线程池配置参数
     */
    private final ThreadPoolProperties properties;

    public DynamicThreadPoolManager(ThreadPoolProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void init() {
        // 这里暂时只支持一个线程池配置，后期支持多个
        if (null != properties && properties.isLazyModeEnabled()) {
            createAndCacheExecutor(properties);
        }
    }

    /**
     * 通过名称获取线程池执行器
     *
     * @param threadPoolName
     * @return
     */
    public DynamicThreadPoolExecutor getExecutor(String threadPoolName) {
        return executorContainer.get(threadPoolName);
    }

    /**
     * 通过名称获取线程池执行器, 如果不存在则创建
     *
     * @param threadPoolName
     * @return
     */
    public DynamicThreadPoolExecutor getExecutorIfNullCreate(String threadPoolName) {

        DynamicThreadPoolExecutor threadPoolExecutor = executorContainer.get(threadPoolName);
        if (threadPoolExecutor != null) {
            return threadPoolExecutor;
        }

        ThreadPoolProperties defaultConfigProperties = new ThreadPoolProperties();
        defaultConfigProperties.setThreadPoolName(threadPoolName);

        return createAndCacheExecutor(properties);
    }

    private void checkThreadPoolProperties(ThreadPoolProperties properties) {
        assert properties != null;

    }

    /**
     * 创建并缓存线程池执行器
     *
     * @param properties
     * @throws ExecutorCreateFailureException
     *          If the thread pool name is duplicated.
     */
    private synchronized DynamicThreadPoolExecutor createAndCacheExecutor(ThreadPoolProperties properties) {

        checkThreadPoolProperties(properties);

        if (executorContainer.containsKey(properties.getThreadPoolName())) {
            throw new ExecutorCreateFailureException("The thread pool name '" + properties.getThreadPoolName() + "' already exists.");
        }

        DynamicThreadPoolExecutor dynamicThreadPoolExecutor = threadPoolFactory.create(properties);
        executorContainer.putIfAbsent(properties.getThreadPoolName(), dynamicThreadPoolExecutor);

        return dynamicThreadPoolExecutor;
    }

    public synchronized void refreshExecutor(ThreadPoolProperties properties) {
        if (!executorContainer.containsKey(properties.getThreadPoolName())) {
            log.warn("The thread pool name '{}' doesn't exists, do not need to refresh.", properties.getThreadPoolName());
            return;
        }

        DynamicThreadPoolExecutor threadPoolExecutor = executorContainer.get(properties.getThreadPoolName());

        doRefresh(threadPoolExecutor, properties);
    }

    private void doRefresh(DynamicThreadPoolExecutor executor, ThreadPoolProperties properties) {
        executor.setCorePoolSize(properties.getCorePoolSize());
        executor.setMaximumPoolSize(properties.getMaximumPoolSize());
        executor.setKeepAliveTime(properties.getKeepAliveTime(), properties.getTimeUnit());
        executor.setRejectedExecutionHandler(RejectedExecutionBuilder.build(properties.getRejectedExecutionType()));
    }
}
