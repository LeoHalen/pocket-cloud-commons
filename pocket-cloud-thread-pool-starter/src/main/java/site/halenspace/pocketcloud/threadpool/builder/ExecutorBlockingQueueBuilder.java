package site.halenspace.pocketcloud.threadpool.builder;

import site.halenspace.pocketcloud.threadpool.DynamicThreadPoolExecutor;
import site.halenspace.pocketcloud.threadpool.consts.QueueTypeConst;
import site.halenspace.pocketcloud.threadpool.consts.RejectedExecutionHandlerTypeConst;

import java.util.concurrent.*;

/**
 * 线程池执行器阻塞队列构建
 *
 * @author Halen.Leo · 2021/7/6
 */
public class ExecutorBlockingQueueBuilder {

    public static BlockingQueue<Runnable> build(String queueType, int queueCapacity, boolean fair) {
        switch (queueType) {
            case QueueTypeConst.ArrayBlockingQueue:
                return new ArrayBlockingQueue<>(queueCapacity);
            case QueueTypeConst.LinkedBlockingQueue:
                return new LinkedBlockingQueue<>(queueCapacity);
            case QueueTypeConst.LinkedBlockingDeque:
                return new LinkedBlockingDeque<>(queueCapacity);
            case QueueTypeConst.DelayQueue:
                return new DelayQueue();
            case QueueTypeConst.SynchronousQueue:
                return new SynchronousQueue<>(fair);
            case QueueTypeConst.PriorityBlockingQueue:
                return new PriorityBlockingQueue<>();
        }

        return new ArrayBlockingQueue<>(queueCapacity);
    }
}
