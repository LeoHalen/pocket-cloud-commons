package site.halenspace.pocketcloud.threadpool.builder;

import site.halenspace.pocketcloud.threadpool.DynamicThreadPoolExecutor;
import site.halenspace.pocketcloud.threadpool.consts.RejectedExecutionHandlerTypeConst;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池拒绝策略构建
 *
 * @author Halen Leo · 2021/7/6
 */
public class RejectedExecutionBuilder {

    public static RejectedExecutionHandler build(String rejectedExecutionType) {
        switch (rejectedExecutionType) {
            case RejectedExecutionHandlerTypeConst.AbortPolicy:
                return new ThreadPoolExecutor.AbortPolicy();
            case RejectedExecutionHandlerTypeConst.CallerRunsPolicy:
                return new ThreadPoolExecutor.CallerRunsPolicy();
            case RejectedExecutionHandlerTypeConst.DiscardOldestPolicy:
                return new ThreadPoolExecutor.DiscardOldestPolicy();
            case RejectedExecutionHandlerTypeConst.DiscardPolicy:
                return new ThreadPoolExecutor.DiscardPolicy();
        }

        return new DynamicThreadPoolExecutor.DynamicDiscardPolicy();
    }
}
