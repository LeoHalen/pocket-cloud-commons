package site.halenspace.pocketcloud.threadpool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import site.halenspace.pocketcloud.threadpool.properties.ThreadPoolProperties;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 *
 * @Author Halen.Leo · 2021/7/2
 * @Blogger 后起小生
 * @Github https://github.com/LeoHalen
 */
@Slf4j
@Component
public class DynamicThreadPoolManager {

    /**
     * 动态线程池执行器容器
     *  ConcurrentMap.Entry => {
     *      key: ThreadPoolName,
     *      value: DynamicThreadPoolExecutor
     *  }
     */
    private static final ConcurrentMap<String, DynamicThreadPoolExecutor> executorContainer = new ConcurrentHashMap<>();

    /**
     * 线程池配置参数
     */
    private final ThreadPoolProperties properties;

    public DynamicThreadPoolManager(ThreadPoolProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void init() {
        // 这里暂时只支持一个线程池配置，后期支持多个
        if (null != properties && properties.isLazyModeEnabled()) {

        }
    }

//    public void threadPoolCreate(ThreadPoolProperties properties) {
//        DynamicThreadPoolFactory
//    }
}
