package site.halenspace.pocketcloud.threadpool.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.halenspace.pocketcloud.threadpool.DynamicThreadPoolExecutor;
import site.halenspace.pocketcloud.threadpool.DynamicThreadPoolManager;
import site.halenspace.pocketcloud.threadpool.strategy.properties.ThreadPoolProperties;
import site.halenspace.pocketcloud.threadpool.request.UpdateThreadPoolReq;

/**
 * @author Halen Leo · 2021/7/6
 */
@Slf4j
//@RestController
@RequestMapping("/test")
public class TestExecutorController {

    private static final String threadPoolName = "testExecutor";

    @GetMapping("executor")
    public void testController() {
        DynamicThreadPoolExecutor executor = DynamicThreadPoolManager.getInstance().getExecutorIfNullCreate(threadPoolName);

        executor.execute(() -> {
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

        if (!DynamicThreadPoolManager.getInstance().contains(reqParam.getThreadPoolName())) {
            return "不好意思让你失望了，你要找的它不存在！";
        }

        ThreadPoolProperties properties = new ThreadPoolProperties();
        BeanUtils.copyProperties(reqParam, properties);
        DynamicThreadPoolManager.getInstance().refreshExecutor(properties);

        return "修改成功";
    }
}
