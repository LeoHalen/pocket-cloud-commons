package site.halenspace.pocketcloud.threadpool;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Halen.Leo · 2021/7/6
 */
//@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { TestApplication.class, DynamicThreadPoolAutoConfiguration.class})
public class DynamicThreadPoolManagerTest {

    @Autowired
    private DynamicThreadPoolManager dynamicThreadPoolManager;

    private static final String threadPoolName = "testExecutor";

    @Test
    public void testCreateDynamicThreadPoolExecutor() {

        DynamicThreadPoolExecutor executor = dynamicThreadPoolManager.getExecutorIfNullCreate(threadPoolName);

        executor.execute(() -> {
            String currentThreadName = Thread.currentThread().getName();
            System.out.println("当前线程名：" + currentThreadName);
        });
    }
}
