package site.halenspace.pocketcloud.threadpool;

import lombok.extern.slf4j.Slf4j;
import site.halenspace.pocketcloud.threadpool.builder.RejectedExecutionBuilder;
import site.halenspace.pocketcloud.threadpool.exeception.ExecutorCreateFailureException;
import site.halenspace.pocketcloud.threadpool.exeception.ExecutorRefreshFailureException;
import site.halenspace.pocketcloud.threadpool.strategy.properties.DynamicThreadPoolProperties;
import site.halenspace.pocketcloud.threadpool.strategy.properties.factory.DynamicThreadPoolPropertiesFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;

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
//    private static final ThreadPoolFactory<DynamicThreadPoolExecutor,
//            DynamicThreadPoolKey, DynamicThreadPoolProperties.Setter> threadPoolFactory = new DynamicThreadPoolFactory();
    /** 线程池配置参数 */
    private final List<DynamicThreadPoolProperties> propertiesContainer = new ArrayList<>();
    /** 实例 */
    private static final DynamicThreadPoolManager INSTANCE = new DynamicThreadPoolManager();


    private DynamicThreadPoolManager() {
    }

//    public DynamicThreadPoolManager() {
//    }
//
//    public DynamicThreadPoolManager(ThreadPoolProperties properties) {
//        this.propertiesContainer.add(properties);
//        this.init();
//    }
//
//    public DynamicThreadPoolManager(List<ThreadPoolProperties> properties) {
//        this.propertiesContainer.addAll(properties);
//        this.init();
//    }

//    @PostConstruct
//    public void init() {
//        propertiesContainer.stream()
//                .filter(p -> !p.isLazyModeEnabled())
//                .forEach(this::createAndCacheExecutor);
//    }

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
     * @param threadPoolKey
     * @return
     */
    public DynamicThreadPoolExecutor getExecutorIfNullCreate(DynamicThreadPoolKey threadPoolKey) {
        DynamicThreadPoolExecutor threadPoolExecutor = executorContainer.get(threadPoolKey.name());
        if (threadPoolExecutor != null) {
            return threadPoolExecutor;
        }
        return createAndCacheExecutor(threadPoolKey, DynamicThreadPoolProperties.Setter());
    }

    private boolean existed(DynamicThreadPoolKey threadPoolKey) {
        return executorContainer.containsKey(threadPoolKey.name());
    }

    /**
     * 创建并缓存线程池执行器
     *
     * @throws ExecutorCreateFailureException
     *          If the thread pool name is duplicated.
     */
    private synchronized ExecutorService createAndCacheExecutor(DynamicThreadPoolKey threadPoolKey, DynamicThreadPoolProperties.Setter builder) {
//        if (threadPoolExisted(threadPoolKey)) {
//            throw new ExecutorCreateFailureException("The thread pool name '" + threadPoolKey.name() + "' already exists.");
//        }
//        final DynamicThreadPoolProperties properties = DynamicThreadPoolPropertiesFactory.getThreadPoolProperties(threadPoolKey, builder);
//        DynamicThreadPoolExecutor dynamicThreadPoolExecutor = DynamicThreadPoolFactory.getInstance().getThreadPool(threadPoolKey, properties);
//        executorContainer.putIfAbsent(threadPoolKey.name(), dynamicThreadPoolExecutor);

        DynamicThreadPool dynamicThreadPool = DynamicThreadPool.Factory.getInstance(threadPoolKey, builder);
        return dynamicThreadPool.getExecutor();
    }

    /**
     * 刷新线程池执行器参数
     *
     * @throws ExecutorRefreshFailureException
     *          If the thread pool name does not exist.
     */
    public synchronized void refreshExecutor(DynamicThreadPoolKey threadPoolKey, DynamicThreadPoolProperties.Setter builder) throws ExecutorRefreshFailureException {
        if (!threadPoolExisted(threadPoolKey)) {
            throw new ExecutorRefreshFailureException("The thread pool name '" + threadPoolKey.name() + "' doesn't exist.");
        }
        DynamicThreadPoolExecutor threadPoolExecutor = executorContainer.get(threadPoolKey.name());
        doRefresh(threadPoolExecutor, properties);
    }

    private void doRefresh(DynamicThreadPoolExecutor executor, DynamicThreadPoolProperties properties) {
        executor.setCorePoolSize(properties.getCorePoolSize().get());
        executor.setMaximumPoolSize(properties.getMaximumPoolSize().get());
        executor.setKeepAliveTime(properties.getKeepAliveTime().get(), properties.getDefaultTimeUnit());
        executor.setRejectedExecutionHandler(RejectedExecutionBuilder.build(properties.getRejectedExecutionType().get()));
    }
}
