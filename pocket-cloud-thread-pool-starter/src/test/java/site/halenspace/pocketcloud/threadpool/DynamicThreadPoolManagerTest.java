package site.halenspace.pocketcloud.threadpool;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import site.halenspace.pocketcloud.threadpool.strategy.properties.DynamicThreadPoolProperties;

import java.beans.PropertyChangeSupport;
import java.util.concurrent.ExecutorService;

/**
 * @author Halen Leo · 2021/7/6
 */
//@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { TestApplication.class, DynamicThreadPoolAutoConfiguration.class})
public class DynamicThreadPoolManagerTest {

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
    public void mockThreadPoolExecutorDynamicConfigValidity() {

    }
}
