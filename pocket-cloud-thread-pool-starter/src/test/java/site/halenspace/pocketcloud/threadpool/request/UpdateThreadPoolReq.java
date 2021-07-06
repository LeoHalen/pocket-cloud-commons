package site.halenspace.pocketcloud.threadpool.request;

import lombok.Data;
import site.halenspace.pocketcloud.threadpool.consts.QueueTypeConst;
import site.halenspace.pocketcloud.threadpool.consts.RejectedExecutionHandlerTypeConst;

/**
 * @author Halen.Leo · 2021/7/6
 */
@Data
public class UpdateThreadPoolReq {

    /**
     * 线程池名称, 默认值为 "dynamic-thread-pool"
     */
    private String threadPoolName;

    /**
     * 核心线程数, 默认值为CPU核心数量
     */
    private Integer corePoolSize;

    /**
     * 最大线程数, 默认值为(2 * CPU核心数量)
     */
    private Integer maximumPoolSize;

    /**
     * 空闲线程存活时间
     */
    private long keepAliveTime;

    /**
     * 队列类型
     * @see QueueTypeConst
     */
    private String queueType;

    /**
     * 拒绝策略, 默认丢弃任务并抛出异常
     * @see RejectedExecutionHandlerTypeConst
     */
    private String rejectedExecutionType;
}
