package site.halenspace.pocketcloud.threadpool;

import lombok.extern.slf4j.Slf4j;
import site.halenspace.pocketcloud.threadpool.builder.RejectedExecutionBuilder;
import site.halenspace.pocketcloud.threadpool.exeception.ExecutorCreateFailureException;
import site.halenspace.pocketcloud.threadpool.strategy.properties.DynamicThreadPoolProperties;
import site.halenspace.pocketcloud.threadpool.exeception.ExecutorRefreshFailureException;
import site.halenspace.pocketcloud.threadpool.properties.ThreadPoolProperties;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 动态线程池管理器
 *
 * @author Halen Leo · 2021/7/2
 * @Blogger 后起小生
 * @Github https://github.com/LeoHalen
 */
@Slf4j
//@Component
public class DynamicThreadPoolManager {

    /**
     * 动态线程池执行器容器
     *  ConcurrentMap.Entry => {
     *      key: ThreadPoolName,
     *      value: DynamicThreadPoolExecutor
     *  }
     */
    private static final ConcurrentMap<String, DynamicThreadPoolExecutor> executorContainer = new ConcurrentHashMap<>();
    /** 线程池工厂 */
    private final ThreadPoolFactory<DynamicThreadPoolExecutor, ThreadPoolProperties> threadPoolFactory = new DynamicThreadPoolFactory();
    /** 线程池配置参数 */
    private final List<ThreadPoolProperties> propertiesContainer = new ArrayList<>();
    /** 实例 */
    private static final DynamicThreadPoolManager INSTANCE = new DynamicThreadPoolManager();

    /**
     * 线程池工厂
     */
    private static final ThreadPoolFactory<DynamicThreadPoolExecutor,
            DynamicThreadPoolKey, DynamicThreadPoolProperties.Setter> threadPoolFactory = new DynamicThreadPoolFactory();

    /**
     * 线程池配置参数
     */
    private final DynamicThreadPoolProperties properties;
    public DynamicThreadPoolManager() {
    }

    public DynamicThreadPoolManager(DynamicThreadPoolProperties properties) {
        this.properties = properties;
    public DynamicThreadPoolManager(ThreadPoolProperties properties) {
        this.propertiesContainer.add(properties);
        this.init();
    }

    public DynamicThreadPoolManager(List<ThreadPoolProperties> properties) {
        this.propertiesContainer.addAll(properties);
        this.init();
    }

    @PostConstruct
    public void init() {
        // 这里暂时只支持一个线程池配置，后期支持多个
        if (null != properties && properties.isLazyModeEnabled()) {
            createAndCacheExecutor(properties);
        }
        propertiesContainer.stream()
                .filter(p -> !p.isLazyModeEnabled())
                .forEach(this::createAndCacheExecutor);
    }

    public static DynamicThreadPoolManager getInstance() {
        return INSTANCE;
    }

    /**
     * 判断线程池是否存在
     *
     * @param threadPoolName
     * @return
     */
    public boolean contains(String threadPoolName) {
        return executorContainer.containsKey(threadPoolName);
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

        DynamicThreadPoolProperties defaultConfigProperties = new DynamicThreadPoolProperties(DynamicThreadPoolKey.Factory.asKey(threadPoolName));

        return createAndCacheExecutor(defaultConfigProperties);
    }

    private void checkDynamicThreadPoolProperties(DynamicThreadPoolProperties properties) {
        assert properties != null;
    }

    private boolean threadPoolExisted(ThreadPoolProperties properties) {
        return executorContainer.containsKey(properties.getThreadPoolName());
    }

    /**
     * 创建并缓存线程池执行器
     *
     * @param properties
     * @throws ExecutorCreateFailureException
     *          If the thread pool name is duplicated.
     */
    private synchronized DynamicThreadPoolExecutor createAndCacheExecutor(DynamicThreadPoolProperties properties) {

        checkDynamicThreadPoolProperties(properties);

        if (threadPoolExisted(properties)) {
            throw new ExecutorCreateFailureException("The thread pool name '" + properties.getThreadPoolName() + "' already exists.");
        }
        DynamicThreadPoolExecutor dynamicThreadPoolExecutor = threadPoolFactory.create(properties);
        executorContainer.putIfAbsent(properties.getThreadPoolName(), dynamicThreadPoolExecutor);
        return dynamicThreadPoolExecutor;
    }

    /**
     * 刷新线程池执行器参数
     *
     * @param properties
     * @throws ExecutorRefreshFailureException
     *          If the thread pool name does not exist.
     */
    public synchronized void refreshExecutor(ThreadPoolProperties properties) throws ExecutorRefreshFailureException {
        if (!threadPoolExisted(properties)) {
            throw new ExecutorRefreshFailureException("The thread pool name '" + properties.getThreadPoolName() + "' doesn't exist.");
        }
        DynamicThreadPoolExecutor threadPoolExecutor = executorContainer.get(properties.getThreadPoolName());
        doRefresh(threadPoolExecutor, properties);
    }

    private void doRefresh(DynamicThreadPoolExecutor executor, DynamicThreadPoolProperties properties) {
        executor.setCorePoolSize(properties.getCorePoolSize());
        executor.setMaximumPoolSize(properties.getMaximumPoolSize());
        executor.setKeepAliveTime(properties.getKeepAliveTime(), properties.getTimeUnit());
        executor.setRejectedExecutionHandler(RejectedExecutionBuilder.build(properties.getRejectedExecutionType()));
    }
}
