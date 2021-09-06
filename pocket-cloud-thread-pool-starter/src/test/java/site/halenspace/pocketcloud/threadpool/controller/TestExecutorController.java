package site.halenspace.pocketcloud.threadpool.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import site.halenspace.pocketcloud.threadpool.DynamicThreadPoolKey;
import site.halenspace.pocketcloud.threadpool.DynamicThreadPoolManager;
import site.halenspace.pocketcloud.threadpool.request.UpdateThreadPoolReq;

import java.util.concurrent.ExecutorService;

/**
 * @author Halen Leo · 2021/7/6
 */
@Slf4j
//@RestController
@RequestMapping("/test")
public class TestExecutorController {

    private final DynamicThreadPoolKey threadPoolKey = DynamicThreadPoolKey.Factory.asKey("test_pool_key");

    @GetMapping("executor")
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

    @PostMapping("executor")
    public String updateExecutorConfig(@RequestBody UpdateThreadPoolReq reqParam) {

        DynamicThreadPoolKey threadPoolKey = DynamicThreadPoolKey.Factory.asKey(reqParam.getThreadPoolName());

        if (!DynamicThreadPoolManager.getInstance().contains(threadPoolKey)) {
            return "不好意思让你失望了，你要找的它不存在！";
        }

        DynamicThreadPoolManager.getInstance().refreshExecutor(threadPoolKey);

        return "修改成功";
    }
}
