package site.halenspace.pocketcloud.threadpool.springmock;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import site.halenspace.pocketcloud.threadpool.*;
import site.halenspace.pocketcloud.threadpool.strategy.properties.DynamicThreadPoolProperties;

import java.util.concurrent.ExecutorService;

/**
 * @author Halen Leo · 2021/7/6
 */
//@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { TestApplication.class, DynamicThreadPoolAutoConfiguration.class})
public class DynamicThreadPoolManageSpringBootMock {

    private final DynamicThreadPoolKey threadPoolKey = DynamicThreadPoolKey.Factory.asKey("testExecutor");

    @Test
    public void mockCreateDynamicThreadPoolExecutor() {

//        DynamicThreadPoolExecutor executor = dynamicThreadPoolManager.getExecutorIfNullCreate(threadPoolName);
//
//        executor.execute(() -> {
//            String currentThreadName = Thread.currentThread().getName();
//            System.out.println("当前线程名：" + currentThreadName);
//        });
    }

    @Test
    public void mockRefreshDynamicThreadPoolExecutor() {

//        PropertyChangeSupport changeSupport = new PropertyChangeSupport();
//        changeSupport.addPropertyChangeListener(evt -> {
//            System.out.println(evt);
//            System.out.println("属性变更了");
//        });
//
//        String oldThreadPoolName = threadPoolProperties.getThreadPoolName();
//        threadPoolProperties.setThreadPoolName("更新线程池名称");
//        changeSupport.firePropertyChange("threadPoo", oldThreadPoolName, threadPoolProperties.getThreadPoolName());
    }

    @Test
    public void mockTwoWaysGetThreadPoolExecutorInstanceAreEqual() {
        ExecutorService executorService = DynamicThreadPoolManager.getInstance().getExecutorOrCreateDefault(threadPoolKey);
        DynamicThreadPool threadPool = DynamicThreadPoolManager.getInstance().createAndCacheThreadPool(threadPoolKey, DynamicThreadPoolProperties.Setter());

        executorService.execute(() -> {
            String currentThreadName = Thread.currentThread().getName();
            System.out.println("Test-1实例 | 当前线程名：" + currentThreadName);
        });

        threadPool.getExecutor().execute(() -> {
            String currentThreadName = Thread.currentThread().getName();
            System.out.println("Test-2实例 | 当前线程名：" + currentThreadName);
        });

        System.out.println("Test-1实例Hash值: " + executorService.hashCode() + ", Test-2实例Hash值: " + threadPool.getExecutor().hashCode());
    }

    @Test
    public void mockThreadPoolExecutorDynamicConfigValidity() throws InterruptedException {
        System.setProperty("dynamic.threadpool.testExecutor.keepAliveTime", "10");
        System.setProperty("dynamic.threadpool.testExecutor.corePoolSize", "2");
        ExecutorService executorService = DynamicThreadPoolManager.getInstance().getExecutorOrCreateDefault(threadPoolKey);
        System.setProperty("dynamic.threadpool.testExecutor.corePoolSize", "1");
        int i = 10;
        while (i-- > 1) {
//            if (i == 90) {
//                DynamicThreadPoolManager.getInstance().refreshExecutor(threadPoolKey);
//            }
            Thread.sleep(1000);
            executorService.execute(() -> {
                String currentThreadName = Thread.currentThread().getName();
                System.out.println(threadPoolKey + "线程池实例 | 当前线程名：" + currentThreadName);
            });
        }
        DynamicThreadPoolManager.getInstance().refreshExecutor(threadPoolKey);
        System.out.println("刷新线程池");
        i = 20;
        while (i-- > 1) {
            Thread.sleep(1000);
            executorService.execute(() -> {
                String currentThreadName = Thread.currentThread().getName();
                System.out.println(threadPoolKey + "线程池实例 | 当前线程名：" + currentThreadName + " => 是否被中断：" + Thread.currentThread().isInterrupted());
            });
        }
        System.out.println("corePoolSize: " + System.getProperty("dynamic.threadpool.testExecutor.corePoolSize"));
    }
}
