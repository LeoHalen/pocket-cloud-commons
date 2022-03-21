package site.halenspace.pocket.threadpool;

import lombok.extern.slf4j.Slf4j;
import site.halenspace.pocket.threadpool.exception.ThreadPoolKeyNotFoundException;
import site.halenspace.pocket.threadpool.properties.strategy.DynamicThreadPoolProperties;

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
     * Use {@link DynamicThreadPoolKey} to check whether the {@link DynamicThreadPoolExecutor} exists
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
     * Gets the {@link ExecutorService} implements {@link DynamicThreadPoolExecutor} by {@link DynamicThreadPoolKey}
     *
     * @param threadPoolKey
     *          {@link DynamicThreadPoolKey} representing the name or type of {@link DynamicThreadPool}
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
     * <p>
     *  Gets the {@link ExecutorService} implements {@link DynamicThreadPoolExecutor} by {@link DynamicThreadPoolKey}
     *      and create the default if it doesn't exist.
     * </p>
     *
     * @param threadPoolKey
     *          {@link DynamicThreadPoolKey} representing the name or type of {@link DynamicThreadPool}
     * @return
     */
    public ExecutorService getExecutorOrCreateDefault(DynamicThreadPoolKey threadPoolKey) {
        DynamicThreadPool dynamicThreadPool = DynamicThreadPool.Factory.getInstance(threadPoolKey, DynamicThreadPoolProperties.Setter());
        return dynamicThreadPool.getExecutor();
    }

    /**
     * Create and cache {@link DynamicThreadPool}
     *
     * @param threadPoolKey
     *          {@link DynamicThreadPoolKey} representing the name or type of {@link DynamicThreadPool}
     * @param builder
     *          the builder to use default {@link DynamicThreadPoolProperties.Setter}
     * @return
     */
    public DynamicThreadPool createAndCacheThreadPool(DynamicThreadPoolKey threadPoolKey, DynamicThreadPoolProperties.Setter builder) {
        return DynamicThreadPool.Factory.getInstance(threadPoolKey, builder);
    }

    /**
     * Refresh Configuration updates to the {@link DynamicThreadPoolExecutor}.
     *
     * @param threadPoolKey
     *          {@link DynamicThreadPoolKey} representing the name or type of {@link DynamicThreadPool}
     * @throws ThreadPoolKeyNotFoundException
     *          If the {@link DynamicThreadPoolKey} not found.
     */
    public void refreshExecutor(DynamicThreadPoolKey threadPoolKey) throws ThreadPoolKeyNotFoundException {
        DynamicThreadPool dynamicThreadPool = DynamicThreadPool.Factory.getInstance(threadPoolKey);
        if (dynamicThreadPool == null) {
            throw new ThreadPoolKeyNotFoundException("The thread pool '" + threadPoolKey.name() + "' not found.");
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

    /**
     * Add {@link ExecutorListener} to {@link DynamicThreadPoolExecutor} for the {@link DynamicThreadPoolKey}
     *
     * @param threadPoolKey
     *          {@link DynamicThreadPoolKey} representing the name or type of {@link DynamicThreadPool}
     * @param listener
     *          {@link ExecutorListener} implementation that will be add to {@link DynamicThreadPoolExecutor}
     * @throws ThreadPoolKeyNotFoundException
     *          If the {@link DynamicThreadPoolKey} not found.
     */
    public void addExecutorListener(DynamicThreadPoolKey threadPoolKey, ExecutorListener listener) throws ThreadPoolKeyNotFoundException {
        assert listener != null;
        DynamicThreadPool dynamicThreadPool = DynamicThreadPool.Factory.getInstance(threadPoolKey);
        if (dynamicThreadPool == null) {
            throw new ThreadPoolKeyNotFoundException("The thread pool key '" + threadPoolKey.name() + "' not found.");
        }
        if (log.isDebugEnabled()) {
            log.debug("Thread pool manager add listener for: ThreadPoolKey '{}'", threadPoolKey.name());
        }
        ((DynamicThreadPoolExecutor) dynamicThreadPool.getExecutor()).addListener(listener);
    }
}
