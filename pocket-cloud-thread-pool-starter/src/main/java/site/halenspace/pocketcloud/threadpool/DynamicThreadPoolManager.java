package site.halenspace.pocketcloud.threadpool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import site.halenspace.pocketcloud.threadpool.exeception.ExecutorCreateFailureException;
import site.halenspace.pocketcloud.threadpool.properties.ThreadPoolProperties;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
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
    public DynamicThreadPoolExecutor getThreadPoolExecutor(String threadPoolName) {

        DynamicThreadPoolExecutor threadPoolExecutor = executorContainer.get(threadPoolName);
        if (threadPoolExecutor != null) {
            return threadPoolExecutor;
        }

        ThreadPoolProperties defaultConfigProperties = new ThreadPoolProperties();
        defaultConfigProperties.setThreadPoolName(threadPoolName);

        return createAndCacheExecutor(properties);
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

    private void checkThreadPoolProperties(ThreadPoolProperties properties) {
        assert properties != null;

    }
}
