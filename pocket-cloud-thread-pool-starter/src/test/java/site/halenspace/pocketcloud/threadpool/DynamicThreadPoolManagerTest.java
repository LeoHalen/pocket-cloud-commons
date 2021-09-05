package site.halenspace.pocketcloud.threadpool;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import site.halenspace.pocketcloud.threadpool.strategy.properties.DynamicThreadPoolProperties;

import java.beans.PropertyChangeSupport;

/**
 * @author Halen Leo · 2021/7/6
 */
//@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { TestApplication.class, DynamicThreadPoolAutoConfiguration.class})
public class DynamicThreadPoolManagerTest {

//    @Autowired
//    private DynamicThreadPoolManager dynamicThreadPoolManager;

    private static final String threadPoolName = "testExecutor";

    @Test
    public void testCreateDynamicThreadPoolExecutor() {

//        DynamicThreadPoolExecutor executor = dynamicThreadPoolManager.getExecutorIfNullCreate(threadPoolName);
//
//        executor.execute(() -> {
//            String currentThreadName = Thread.currentThread().getName();
//            System.out.println("当前线程名：" + currentThreadName);
//        });
    }

    @Test
    public void testRefreshDynamicThreadPoolExecutor() {

        DynamicThreadPoolProperties threadPoolProperties = new DynamicThreadPoolProperties();
//
//        DynamicThreadPoolExecutor executor = dynamicThreadPoolManager.getExecutorIfNullCreate(threadPoolName);
//
//        executor.execute(() -> {
//            String currentThreadName = Thread.currentThread().getName();
//            System.out.println("当前线程名：" + currentThreadName);
//        });

        PropertyChangeSupport changeSupport = new PropertyChangeSupport(threadPoolProperties);
        changeSupport.addPropertyChangeListener(evt -> {
            System.out.println(evt);
            System.out.println("属性变更了");
        });

        String oldThreadPoolName = threadPoolProperties.getThreadPoolName();
        threadPoolProperties.setThreadPoolName("更新线程池名称");
        changeSupport.firePropertyChange("threadPoo", oldThreadPoolName, threadPoolProperties.getThreadPoolName());
    }
}
