package site.halenspace.pocketcloud.threadpool.springmock.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.*;
import site.halenspace.pocketcloud.threadpool.DynamicThreadPoolKey;
import site.halenspace.pocketcloud.threadpool.DynamicThreadPoolManager;
import site.halenspace.pocketcloud.threadpool.springmock.request.UpdateThreadPoolReq;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author Halen Leo · 2021/7/6
 */
@Slf4j
@RestController
@RequestMapping("/test/executor")
public class TestExecutorController {

    private final DynamicThreadPoolKey threadPoolKey = DynamicThreadPoolKey.Factory.asKey("test-executor");

    @GetMapping("test1")
    public void testController() {
        ExecutorService executorService = DynamicThreadPoolManager.getInstance().getExecutorOrCreateDefault(threadPoolKey);

        executorService.execute(() -> {
            String currentThreadName = Thread.currentThread().getName();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("当前线程名：" + currentThreadName);
        });
    }

    @PostMapping("refresh-config")
    public String updateExecutorConfig(@RequestBody UpdateThreadPoolReq reqParam) {

        DynamicThreadPoolKey threadPoolKey = DynamicThreadPoolKey.Factory.asKey(reqParam.getThreadPoolName());

        if (!DynamicThreadPoolManager.getInstance().contains(threadPoolKey)) {
            return "不好意思让你失望了，你要找的它不存在！";
        }

        DynamicThreadPoolManager.getInstance().refreshExecutor(threadPoolKey);

        return "修改成功";
    }

    @PostMapping("refresh-config-is-valid")
    public void testRefreshConfigIsValid(@RequestBody UpdateThreadPoolReq reqParam) throws InterruptedException {
        if (reqParam.getCorePoolSize() != null) {
            System.setProperty("dynamic.threadpool.default.corePoolSize", reqParam.getCorePoolSize().toString());
        }
        if (reqParam.getMaximumPoolSize() != null) {
            System.setProperty("dynamic.threadpool.default.maximumPoolSize", reqParam.getMaximumPoolSize().toString());
        }
        DynamicThreadPoolKey threadPoolKey = DynamicThreadPoolKey.Factory.asKey("threadExecutor");

        ExecutorService executorService = DynamicThreadPoolManager.getInstance().getExecutorOrCreateDefault(threadPoolKey);

        int i = 50;
        while (i-- > 1) {
//            Thread.sleep(1000);
            executorService.execute(() -> {
                String currentThreadName = Thread.currentThread().getName();
                System.out.println(threadPoolKey + "线程池实例 | 当前线程名：" + currentThreadName);
            });
        }
    }

    @GetMapping("v1/refresh-config-is-valid")
    public void testRefreshConfigIsValid() {
        Map<String, String> getenv = System.getenv();
        System.setProperty("thread.pool.plugin.ThreadPoolDynamicProperties.implementation",
                "site.halenspace.pocketcloud.threadpool.javamock.ThreadPoolPluginsMock$ThreadPoolDynamicPropertiesMockSpringBoot");
        System.out.println("corePoolSize: " + System.getProperty("dynamic.threadpool.test-executor.corePoolSize"));
        System.out.println("maximumPoolSize: " + System.getProperty("dynamic.threadpool.test-executor.maximumPoolSize"));
        DynamicThreadPoolKey threadPoolKey = DynamicThreadPoolKey.Factory.asKey("test-executor");

        ExecutorService executorService = DynamicThreadPoolManager.getInstance().getExecutorOrCreateDefault(threadPoolKey);

        int i = 50;
        while (i-- > 1) {
//            Thread.sleep(1000);
            executorService.execute(() -> {
                String currentThreadName = Thread.currentThread().getName();
                System.out.println(threadPoolKey + "线程池实例 | 当前线程名：" + currentThreadName);
            });
        }
    }

    @GetMapping("mock-thread-pool-executor-preheat-all-threads")
    public void mockThreadPoolExecutorPreheatAllThreads() {
        System.setProperty("thread.pool.plugin.ThreadPoolDynamicProperties.implementation",
                "site.halenspace.pocketcloud.threadpool.javamock.ThreadPoolPluginsMock$ThreadPoolDynamicPropertiesMockSpringBoot");
        ExecutorService executorService = DynamicThreadPoolManager.getInstance().getExecutorOrCreateDefault(threadPoolKey);
        System.out.println(threadPoolKey + "线程池实例 | Mock预热所有线程池");
    }
}
