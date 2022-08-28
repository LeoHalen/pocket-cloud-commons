package site.halenspace.pocket.threadpool.test.handler;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import site.halenspace.pocket.threadpool.DynamicThreadPoolExecutor;
import site.halenspace.pocket.threadpool.ThreadPoolHandler;
import site.halenspace.pocket.threadpool.ThreadPoolKey;
import site.halenspace.pocket.threadpool.ThreadPoolManager;
import site.halenspace.pocket.threadpool.config.ThreadPoolConfigDef;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Zg.Li · 2022/8/12
 */
@Slf4j
public class ThreadPoolHandlerTest {

    static {
        System.setProperty("org.slf4j.simpleLogger.log.com.lixiang", "DEBUG");
    }

    private static String getPropName(String name) {
        return "dynamic.thread-pool.test-pool." + name;
    }

    @Test
    public void testGetManagerParamThreadPoolName() throws InterruptedException {
        ThreadPoolManager manager = ThreadPoolHandler.getManager("test-pool");

        assertEquals("test-pool", manager.getThreadPoolKey().get());

        // Assert that the property is equal to the default value.
        assertNotNull(manager.getProperties());
        assertEquals(ThreadPoolConfigDef.DEFAULT_CORE_POOL_SIZE, manager.getProperties().getCorePoolSize().get());
        assertEquals(ThreadPoolConfigDef.DEFAULT_MAXIMUM_POOL_SIZE, manager.getProperties().getMaximumPoolSize().get());
        assertEquals(ThreadPoolConfigDef.DEFAULT_KEEP_ALIVE_TIME, manager.getProperties().getKeepAliveTime().get());

        // Assert that the property is equal to the default value.
        assertNotNull(manager.getExecutor());
        assertEquals(ThreadPoolConfigDef.DEFAULT_CORE_POOL_SIZE.intValue(), ((DynamicThreadPoolExecutor) manager.getExecutor()).getCorePoolSize());
        assertEquals(ThreadPoolConfigDef.DEFAULT_MAXIMUM_POOL_SIZE.intValue(), ((DynamicThreadPoolExecutor) manager.getExecutor()).getMaximumPoolSize());
        assertEquals(ThreadPoolConfigDef.DEFAULT_KEEP_ALIVE_TIME.longValue(), ((DynamicThreadPoolExecutor) manager.getExecutor()).getKeepAliveTime(ThreadPoolConfigDef.DEFAULT_TIMEUNIT));

        // Observe the actual implementation.
        DynamicThreadPoolExecutor executor = (DynamicThreadPoolExecutor) manager.getExecutor();
        CountDownLatch latch = new CountDownLatch(100);
        int i = 100;
        while (i-- > 0) {
            log.info(
                    "{}(activeThreads: {}, corePoolSize: {}, maximumPoolSize: {}, keepAliveTime: {})",
                    manager.getThreadPoolKey().get(),
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
                log.info("线程池实例: {} | 当前线程名: {}", manager.getThreadPoolKey().get(), Thread.currentThread().getName());
                latch.countDown();
            });
        }

        latch.await();
    }

    @Test
    public void testGetManagerParamThreadPoolKey() throws InterruptedException {
        ThreadPoolKey threadPoolKey = ThreadPoolKey.Factory.asKey("test-pool");
        ThreadPoolManager manager = ThreadPoolHandler.getManager(threadPoolKey);

        // Test the ThreadPoolKey creation correctness.
        assertEquals(threadPoolKey.hashCode(), manager.getThreadPoolKey().hashCode());

        // Assert that the property is equal to the default value.
        assertNotNull(manager.getProperties());
        assertEquals(ThreadPoolConfigDef.DEFAULT_CORE_POOL_SIZE, manager.getProperties().getCorePoolSize().get());
        assertEquals(ThreadPoolConfigDef.DEFAULT_MAXIMUM_POOL_SIZE, manager.getProperties().getMaximumPoolSize().get());
        assertEquals(ThreadPoolConfigDef.DEFAULT_KEEP_ALIVE_TIME, manager.getProperties().getKeepAliveTime().get());

        // Assert that the property is equal to the default value.
        assertNotNull(manager.getExecutor());
        assertEquals(ThreadPoolConfigDef.DEFAULT_CORE_POOL_SIZE.intValue(), ((DynamicThreadPoolExecutor) manager.getExecutor()).getCorePoolSize());
        assertEquals(ThreadPoolConfigDef.DEFAULT_MAXIMUM_POOL_SIZE.intValue(), ((DynamicThreadPoolExecutor) manager.getExecutor()).getMaximumPoolSize());
        assertEquals(ThreadPoolConfigDef.DEFAULT_KEEP_ALIVE_TIME.longValue(), ((DynamicThreadPoolExecutor) manager.getExecutor()).getKeepAliveTime(ThreadPoolConfigDef.DEFAULT_TIMEUNIT));

        // Observe the actual implementation.
        DynamicThreadPoolExecutor executor = (DynamicThreadPoolExecutor) manager.getExecutor();
        CountDownLatch latch = new CountDownLatch(100);
        int i = 100;
        while (i-- > 0) {
            log.info(
                    "{}(activeThreads: {}, corePoolSize: {}, maximumPoolSize: {}, keepAliveTime: {})",
                    manager.getThreadPoolKey().get(),
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
                log.info("线程池实例: {} | 当前线程名: {}", manager.getThreadPoolKey().get(), Thread.currentThread().getName());
                latch.countDown();
            });
        }

        latch.await();
    }

    @Test
    public void testGetExecutorParamThreadPoolName() throws InterruptedException {
        System.setProperty(getPropName(ThreadPoolConfigDef.CORE_POOL_SIZE), "10");
        System.setProperty(getPropName(ThreadPoolConfigDef.MAXIMUM_POOL_SIZE), "15");
        System.setProperty(getPropName(ThreadPoolConfigDef.KEEP_ALIVE_TIME), "1500");

        String threadPoolName = "test-pool";
        DynamicThreadPoolExecutor executor = (DynamicThreadPoolExecutor) ThreadPoolHandler.getExecutor(threadPoolName);

        assertNotNull(executor);
        assertEquals(10, executor.getCorePoolSize());
        assertEquals(15, executor.getMaximumPoolSize());
        assertEquals(1500L, executor.getKeepAliveTime(ThreadPoolConfigDef.DEFAULT_TIMEUNIT));

        // Observe the actual implementation.
        CountDownLatch latch = new CountDownLatch(100);
        int i = 100;
        while (i-- > 0) {
            log.info(
                    "{}(activeThreads: {}, corePoolSize: {}, maximumPoolSize: {}, keepAliveTime: {})",
                    threadPoolName,
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
                log.info("线程池实例: {} | 当前线程名: {}", threadPoolName, Thread.currentThread().getName());
                latch.countDown();
            });
        }

        latch.await();
    }

    @Test
    public void testGetExecutorParamThreadPoolKey() throws InterruptedException {
        System.setProperty(getPropName(ThreadPoolConfigDef.CORE_POOL_SIZE), "10");
        System.setProperty(getPropName(ThreadPoolConfigDef.MAXIMUM_POOL_SIZE), "15");
        System.setProperty(getPropName(ThreadPoolConfigDef.KEEP_ALIVE_TIME), "1500");

        ThreadPoolKey threadPoolKey = ThreadPoolKey.Factory.asKey("test-pool");
        DynamicThreadPoolExecutor executor = (DynamicThreadPoolExecutor) ThreadPoolHandler.getExecutor(threadPoolKey);

        assertNotNull(executor);
        assertEquals(10, executor.getCorePoolSize());
        assertEquals(15, executor.getMaximumPoolSize());
        assertEquals(1500L, executor.getKeepAliveTime(ThreadPoolConfigDef.DEFAULT_TIMEUNIT));

        // Observe the actual implementation.
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

    @Test
    public void testRefreshExecutorParamThreadPoolName() throws InterruptedException {
        String threadPoolName = "test-pool";
        DynamicThreadPoolExecutor executor = (DynamicThreadPoolExecutor) ThreadPoolHandler.getExecutor(threadPoolName);

        // Assert that the property is equal to the default value.
        assertNotNull(executor);
        assertEquals(ThreadPoolConfigDef.DEFAULT_CORE_POOL_SIZE.intValue(), executor.getCorePoolSize());
        assertEquals(ThreadPoolConfigDef.DEFAULT_MAXIMUM_POOL_SIZE.intValue(), executor.getMaximumPoolSize());
        assertEquals(ThreadPoolConfigDef.DEFAULT_KEEP_ALIVE_TIME.longValue(), executor.getKeepAliveTime(ThreadPoolConfigDef.DEFAULT_TIMEUNIT));

        // Change the properties and refresh executor instance
        System.setProperty(getPropName(ThreadPoolConfigDef.CORE_POOL_SIZE), "10");
        System.setProperty(getPropName(ThreadPoolConfigDef.MAXIMUM_POOL_SIZE), "15");
        System.setProperty(getPropName(ThreadPoolConfigDef.KEEP_ALIVE_TIME), "1500");
        ThreadPoolHandler.refreshExecutor(threadPoolName);

        // Asserts that the property is equal to the changed value.
        assertEquals(10, executor.getCorePoolSize());
        assertEquals(15, executor.getMaximumPoolSize());
        assertEquals(1500L, executor.getKeepAliveTime(ThreadPoolConfigDef.DEFAULT_TIMEUNIT));

        // Observe the actual implementation.ø
        CountDownLatch latch = new CountDownLatch(100);
        int i = 100;
        while (i-- > 0) {
            log.info(
                    "{}(activeThreads: {}, corePoolSize: {}, maximumPoolSize: {}, keepAliveTime: {})",
                    threadPoolName,
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
                log.info("线程池实例: {} | 当前线程名: {}", threadPoolName, Thread.currentThread().getName());
                latch.countDown();
            });
        }

        latch.await();
    }

    @Test
    public void testRefreshExecutorParamThreadPoolKey() throws InterruptedException {
        ThreadPoolKey threadPoolKey = ThreadPoolKey.Factory.asKey("test-pool");
        ThreadPoolManager manager = ThreadPoolHandler.getManager(threadPoolKey);

        // Assert that the property is equal to the default value.
        assertNotNull(manager.getExecutor());
        assertEquals(ThreadPoolConfigDef.DEFAULT_CORE_POOL_SIZE.intValue(), ((DynamicThreadPoolExecutor) manager.getExecutor()).getCorePoolSize());
        assertEquals(ThreadPoolConfigDef.DEFAULT_MAXIMUM_POOL_SIZE.intValue(), ((DynamicThreadPoolExecutor) manager.getExecutor()).getMaximumPoolSize());
        assertEquals(ThreadPoolConfigDef.DEFAULT_KEEP_ALIVE_TIME.longValue(), ((DynamicThreadPoolExecutor) manager.getExecutor()).getKeepAliveTime(ThreadPoolConfigDef.DEFAULT_TIMEUNIT));

        // Change the properties and refresh executor instance
        System.setProperty(getPropName(ThreadPoolConfigDef.CORE_POOL_SIZE), "10");
        System.setProperty(getPropName(ThreadPoolConfigDef.MAXIMUM_POOL_SIZE), "15");
        System.setProperty(getPropName(ThreadPoolConfigDef.KEEP_ALIVE_TIME), "1500");
        ThreadPoolHandler.refreshExecutor(threadPoolKey);

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
