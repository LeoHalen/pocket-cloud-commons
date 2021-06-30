package site.halenspace.pocketcloud.threadpool.consts;

/**
 * 线程池拒绝策略类型
 *
 * @Author Halen.Leo · 2021/6/30
 * @Blogger 后起小生
 * @Github https://github.com/LeoHalen
 */
public interface RejectedExecutionHandlerTypeConst {

    String CallerRunsPolicy = "CallerRunsPolicy";

    String AbortPolicy = "AbortPolicy";

    String DiscardPolicy = "DiscardPolicy";

    String DiscardOldestPolicy = "DiscardOldestPolicy";
}
