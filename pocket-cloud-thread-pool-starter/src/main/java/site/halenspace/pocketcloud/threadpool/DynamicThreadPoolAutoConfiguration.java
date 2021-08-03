package site.halenspace.pocketcloud.threadpool;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import site.halenspace.pocketcloud.threadpool.properties.ThreadPoolProperties;

/**
 * 动态线程池组件自动配置
 *
 * @author Halen Leo · 2021/6/30
 * @Blogger 后起小生
 * @Github https://github.com/LeoHalen
 */
@ConditionalOnProperty(name = "pocket.dynamic-thread-pool.enabled", matchIfMissing = true)
@EnableConfigurationProperties(ThreadPoolProperties.class)
public class DynamicThreadPoolAutoConfiguration {
}
