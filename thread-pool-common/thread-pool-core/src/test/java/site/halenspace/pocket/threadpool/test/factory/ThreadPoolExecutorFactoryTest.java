package site.halenspace.pocket.threadpool.test.factory;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import site.halenspace.pocket.threadpool.DynamicThreadPoolExecutor;
import site.halenspace.pocket.threadpool.ThreadPoolKey;
import site.halenspace.pocket.threadpool.config.ThreadPoolConfigDef;
import site.halenspace.pocket.threadpool.config.ThreadPoolProperties;
import site.halenspace.pocket.threadpool.factory.ThreadPoolExecutorFactory;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;

/**
 * @author Zg.Li · 2022/8/11
 */
@Slf4j
public class ThreadPoolExecutorFactoryTest {

    private static String getPropName(String name) {
        return "dynamic.thread-pool.test-pool." + name;
    }

    @Test
    public void testGetExecutorOnlyByThreadPoolKey() throws InterruptedException {
        System.setProperty(getPropName(ThreadPoolConfigDef.CORE_POOL_SIZE), "10");
        System.setProperty(getPropName(ThreadPoolConfigDef.MAXIMUM_POOL_SIZE), "15");
        System.setProperty(getPropName(ThreadPoolConfigDef.KEEP_ALIVE_TIME), "1500");


        ThreadPoolKey threadPoolKey = ThreadPoolKey.Factory.asKey("test-pool");
        DynamicThreadPoolExecutor executor = ThreadPoolExecutorFactory.getExecutor(threadPoolKey);
        assertEquals(10, executor.getCorePoolSize());
        assertEquals(15, executor.getMaximumPoolSize());
        assertEquals(1500L, executor.getKeepAliveTime(ThreadPoolConfigDef.DEFAULT_TIMEUNIT));

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

    /**
     * Ensure that the configuration properties of {@link ThreadPoolProperties} implementation class cannot be
     * obtained, so that the custom properties will take effect.
     */
    @Test
    public void testGetExecutor() {
        ThreadPoolKey threadPoolKey = ThreadPoolKey.Factory.asKey("test-pool");
        ThreadPoolProperties threadPoolPropertiesDefault = new ThreadPoolProperties(threadPoolKey);
        // Assert that the property is equal to the default value
        assertEquals(ThreadPoolConfigDef.DEFAULT_CORE_POOL_SIZE, threadPoolPropertiesDefault.getCorePoolSize().get());
        assertEquals(ThreadPoolConfigDef.DEFAULT_MAXIMUM_POOL_SIZE, threadPoolPropertiesDefault.getMaximumPoolSize().get());
        assertEquals(ThreadPoolConfigDef.DEFAULT_KEEP_ALIVE_TIME, threadPoolPropertiesDefault.getKeepAliveTime().get());

        ThreadPoolProperties threadPoolProperties = new ThreadPoolProperties(
                threadPoolKey,
                ThreadPoolProperties.Setter()
                        .withCorePoolSize(5)
                        .withMaximumPoolSize(10)
                        .withKeepAliveTime(1000L)
        );
        DynamicThreadPoolExecutor executor = ThreadPoolExecutorFactory.getExecutor(threadPoolKey, threadPoolProperties);
        assertEquals(5, executor.getCorePoolSize());
        assertEquals(10, executor.getMaximumPoolSize());
        assertEquals(1000L, executor.getKeepAliveTime(ThreadPoolConfigDef.DEFAULT_TIMEUNIT));
    }
}
