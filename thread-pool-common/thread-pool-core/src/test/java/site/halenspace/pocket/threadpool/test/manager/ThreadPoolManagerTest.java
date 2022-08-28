package site.halenspace.pocket.threadpool.test.manager;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import site.halenspace.pocket.threadpool.DynamicThreadPoolExecutor;
import site.halenspace.pocket.threadpool.ThreadPoolKey;
import site.halenspace.pocket.threadpool.ThreadPoolManager;
import site.halenspace.pocket.threadpool.config.ThreadPoolConfigDef;
import site.halenspace.pocket.threadpool.config.ThreadPoolProperties;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Zg.Li · 2022/8/12
 */
@Slf4j
public class ThreadPoolManagerTest {

    private static String getPropName(String name) {
        return "dynamic.thread-pool.test-pool." + name;
    }

    @Test
    public void testConstructorOfParamThreadPoolKey() {
        System.setProperty(getPropName(ThreadPoolConfigDef.CORE_POOL_SIZE), "10");
        System.setProperty(getPropName(ThreadPoolConfigDef.MAXIMUM_POOL_SIZE), "15");
        System.setProperty(getPropName(ThreadPoolConfigDef.KEEP_ALIVE_TIME), "1500");

        ThreadPoolKey threadPoolKey = ThreadPoolKey.Factory.asKey("test-pool");
        ThreadPoolManager manager = new ThreadPoolManager(threadPoolKey);

        // Test the ThreadPoolKey creation correctness.
        assertEquals(threadPoolKey.hashCode(), manager.getThreadPoolKey().hashCode());

        // Test the ThreadPoolProperties creation correctness.
        assertNotNull(manager.getProperties());
        assertEquals(new Integer(10), manager.getProperties().getCorePoolSize().get());
        assertEquals(new Integer(15), manager.getProperties().getMaximumPoolSize().get());
        assertEquals(new Long(1500), manager.getProperties().getKeepAliveTime().get());

        // Test the DynamicThreadPoolExecutor creation correctness.
        assertNotNull(manager.getExecutor());
        assertEquals(10, ((DynamicThreadPoolExecutor) manager.getExecutor()).getCorePoolSize());
        assertEquals(15, ((DynamicThreadPoolExecutor) manager.getExecutor()).getMaximumPoolSize());
        assertEquals(1500L, ((DynamicThreadPoolExecutor) manager.getExecutor()).getKeepAliveTime(ThreadPoolConfigDef.DEFAULT_TIMEUNIT));
    }

    /**
     * Ensure that the configuration properties of {@link ThreadPoolProperties} implementation class cannot be
     * obtained, so that the custom properties will take effect.
     */
    @Test
    public void testConstructorOfParamThreadPoolKeyAndSetter() {
        ThreadPoolKey threadPoolKey = ThreadPoolKey.Factory.asKey("test-pool");
        ThreadPoolProperties threadPoolPropertiesDefault = new ThreadPoolProperties(threadPoolKey);
        // Assert that the property is equal to the default value
        assertEquals(ThreadPoolConfigDef.DEFAULT_CORE_POOL_SIZE, threadPoolPropertiesDefault.getCorePoolSize().get());
        assertEquals(ThreadPoolConfigDef.DEFAULT_MAXIMUM_POOL_SIZE, threadPoolPropertiesDefault.getMaximumPoolSize().get());
        assertEquals(ThreadPoolConfigDef.DEFAULT_KEEP_ALIVE_TIME, threadPoolPropertiesDefault.getKeepAliveTime().get());

        ThreadPoolManager manager = new ThreadPoolManager(
                threadPoolKey,
                ThreadPoolProperties.Setter()
                        .withCorePoolSize(5)
                        .withMaximumPoolSize(10)
                        .withKeepAliveTime(1000L)
        );

        // Test the ThreadPoolKey creation correctness.
        assertEquals(threadPoolKey.hashCode(), manager.getThreadPoolKey().hashCode());

        // Test the ThreadPoolProperties creation correctness.
        assertNotNull(manager.getProperties());
        assertEquals(new Integer(5), manager.getProperties().getCorePoolSize().get());
        assertEquals(new Integer(10), manager.getProperties().getMaximumPoolSize().get());
        assertEquals(new Long(1000), manager.getProperties().getKeepAliveTime().get());

        // Test the DynamicThreadPoolExecutor creation correctness.
        assertNotNull(manager.getExecutor());
        assertEquals(5, ((DynamicThreadPoolExecutor) manager.getExecutor()).getCorePoolSize());
        assertEquals(10, ((DynamicThreadPoolExecutor) manager.getExecutor()).getMaximumPoolSize());
        assertEquals(1000L, ((DynamicThreadPoolExecutor) manager.getExecutor()).getKeepAliveTime(ThreadPoolConfigDef.DEFAULT_TIMEUNIT));
    }

    @Test
    public void testRefreshExecutor() throws InterruptedException {
        ThreadPoolKey threadPoolKey = ThreadPoolKey.Factory.asKey("test-pool");
        ThreadPoolManager manager = new ThreadPoolManager(
                threadPoolKey,
                ThreadPoolProperties.Setter()
                        .withCorePoolSize(5)
                        .withMaximumPoolSize(10)
                        .withKeepAliveTime(1000L)
        );

        // Assert that the property is equal to the default value.
        assertNotNull(manager.getExecutor());
        assertEquals(5, ((DynamicThreadPoolExecutor) manager.getExecutor()).getCorePoolSize());
        assertEquals(10, ((DynamicThreadPoolExecutor) manager.getExecutor()).getMaximumPoolSize());
        assertEquals(1000L, ((DynamicThreadPoolExecutor) manager.getExecutor()).getKeepAliveTime(ThreadPoolConfigDef.DEFAULT_TIMEUNIT));

        // Change the properties and refresh executor instance
        System.setProperty(getPropName(ThreadPoolConfigDef.CORE_POOL_SIZE), "10");
        System.setProperty(getPropName(ThreadPoolConfigDef.MAXIMUM_POOL_SIZE), "15");
        System.setProperty(getPropName(ThreadPoolConfigDef.KEEP_ALIVE_TIME), "1500");
        manager.refreshExecutor();

        // Asserts that the property is equal to the changed value.
        assertEquals(10, ((DynamicThreadPoolExecutor) manager.getExecutor()).getCorePoolSize());
        assertEquals(15, ((DynamicThreadPoolExecutor) manager.getExecutor()).getMaximumPoolSize());
        assertEquals(1500L, ((DynamicThreadPoolExecutor) manager.getExecutor()).getKeepAliveTime(ThreadPoolConfigDef.DEFAULT_TIMEUNIT));

        // Observe the actual implementation.
        DynamicThreadPoolExecutor executor = (DynamicThreadPoolExecutor) manager.getExecutor();
        CountDownLatch latch = new CountDownLatch(100);
        int i = 100;
        while (i-- > 0) {
            log.info(
                    "{}(activeThreads: {}, corePoolSize: {}, maximumPoolSize: {}, keepAliveTime: {})",
                    threadPoolKey.get(),
                    executor.getActiveCount(),
                    executor.getCorePoolSize(),
                    executor.getMaximumPoolSize(),
                    executor.getKeepAliveTime(ThreadPoolConfigDef.DEFAULT_TIMEUNIT)
            );
            executor.execute(() -> {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log.info("线程池实例: {} | 当前线程名: {}", threadPoolKey.get(), Thread.currentThread().getName());
                latch.countDown();
            });
        }

        latch.await();
    }
}
