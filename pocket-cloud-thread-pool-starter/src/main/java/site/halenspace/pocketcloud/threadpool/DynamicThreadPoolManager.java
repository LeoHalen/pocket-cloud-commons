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

//    /**
//     * 动态线程池执行器容器
//     *  ConcurrentMap.Entry => {
//     *      key: ThreadPoolName,
//     *      value: DynamicThreadPoolExecutor
//     *  }
//     */
//    private static final ConcurrentMap<String, DynamicThreadPoolExecutor> executorContainer = new ConcurrentHashMap<>();
//    /** 线程池工厂 */
//    private static final ThreadPoolFactory<DynamicThreadPoolExecutor,
//            DynamicThreadPoolKey, DynamicThreadPoolProperties.Setter> threadPoolFactory = new DynamicThreadPoolFactory();
//    /** 线程池配置参数 */
//    private final List<DynamicThreadPoolProperties> propertiesContainer = new ArrayList<>();

    private static final DynamicThreadPoolManager INSTANCE = new DynamicThreadPoolManager();


    private DynamicThreadPoolManager() {
    }

    public static DynamicThreadPoolManager getInstance() {
        return INSTANCE;
    }

    /**
     * 判断线程池是否存在
     *
     * @param threadPoolKey
     * @return
     */
    public boolean contains(DynamicThreadPoolKey threadPoolKey) {
        DynamicThreadPool dynamicThreadPool = DynamicThreadPool.Factory.getInstance(threadPoolKey);
        if (dynamicThreadPool == null) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 通过名称获取线程池执行器
     *
     * @param threadPoolKey
     * @return
     */
    public ExecutorService getExecutor(DynamicThreadPoolKey threadPoolKey) {
        DynamicThreadPool dynamicThreadPool = DynamicThreadPool.Factory.getInstance(threadPoolKey);
        if (dynamicThreadPool == null) {
            return null;
        }
        return dynamicThreadPool.getExecutor();
    }

    /**
     * 通过名称获取线程池执行器, 如果不存在则创建
     *
     * @param threadPoolKey
     * @return
     */
    public ExecutorService getExecutorOrCreateDefault(DynamicThreadPoolKey threadPoolKey) {
        DynamicThreadPool dynamicThreadPool = DynamicThreadPool.Factory.getInstance(threadPoolKey, DynamicThreadPoolProperties.Setter());
        return dynamicThreadPool.getExecutor();
    }

    /**
     * 创建并缓存线程池执行器
     *
     * @throws ExecutorCreateFailureException
     *          If the {@link DynamicThreadPoolKey} is duplicated.
     */
    public DynamicThreadPool createAndCacheThreadPool(DynamicThreadPoolKey threadPoolKey, DynamicThreadPoolProperties.Setter builder) {
        return DynamicThreadPool.Factory.getInstance(threadPoolKey, builder);
    }


    /**
     * 刷新线程池执行器 {@link DynamicThreadPoolExecutor}.
     *
     * @param threadPoolKey
     * @throws ExecutorRefreshFailureException
     *          If the {@link DynamicThreadPoolKey} not existed.
     */
    public void refreshExecutor(DynamicThreadPoolKey threadPoolKey) throws ExecutorRefreshFailureException {
        DynamicThreadPool dynamicThreadPool = DynamicThreadPool.Factory.getInstance(threadPoolKey);
        if (dynamicThreadPool == null) {
            throw new ExecutorRefreshFailureException("The thread pool '" + threadPoolKey.name() + "' doesn't existed.");
        }
        dynamicThreadPool.refreshExecutor();
    }

//    /**
//     * 刷新线程池执行器参数
//     *
//     * @throws ExecutorRefreshFailureException
//     *          If the thread pool name does not exist.
//     */
//    public synchronized void refreshExecutor(DynamicThreadPoolKey threadPoolKey, DynamicThreadPoolProperties.Setter builder) throws ExecutorRefreshFailureException {
//        if (!threadPoolExisted(threadPoolKey)) {
//            throw new ExecutorRefreshFailureException("The thread pool name '" + threadPoolKey.name() + "' doesn't exist.");
//        }
//        DynamicThreadPoolExecutor threadPoolExecutor = executorContainer.get(threadPoolKey.name());
//    }
}
