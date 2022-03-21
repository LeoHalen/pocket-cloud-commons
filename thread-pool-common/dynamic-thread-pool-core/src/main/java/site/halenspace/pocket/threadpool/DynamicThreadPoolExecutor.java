package site.halenspace.pocket.threadpool;


import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 支持动态设置参数的线程池
 *
 * @author Halen Leo · 2021/6/27
 * @Blogger 后起小生
 * @Github https://github.com/LeoHalen
 */
@Slf4j
public class DynamicThreadPoolExecutor extends ThreadPoolExecutor {

    private Float boundedQueueLoadFactor;
    private Integer boundedQueueSizeWarningThreshold;
    private ExecutorListener listener;
    private Long queueThresholdValveOpenRollingWindowInMinutes;
    private final AtomicBoolean queueThresholdValveOpen = new AtomicBoolean(false);
    private LocalDateTime queueThresholdValveOpenWindowTime;
//    private final AtomicReference<LocalDateTime> queueThresholdValveOpenWindowTime = new AtomicReference<>();
//    private final AtomicInteger queueThresholdValveOpenWindowCounter = new AtomicInteger();
//    private final Timer queueThresholdValveOpenRollingWindowTimer = new Timer();
//    private boolean queueThresholdValveOpenRollingWindow;

    public DynamicThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                     BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public DynamicThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                     BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public DynamicThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                     BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public DynamicThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                     BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    public void execute(Runnable command) {
        super.execute(command);
        boundedQueueMetricsListener();
    }

    private void boundedQueueMetricsListener() {
        if (boundedQueueSizeWarningThreshold == null) {
            return;
        }
        final int waitingTask;
        if ((waitingTask = super.getQueue().size()) >= boundedQueueSizeWarningThreshold) {
            if (queueThresholdValveOpen.compareAndSet(Boolean.FALSE, Boolean.TRUE)) {
                listener.taskThresholdTrigger(boundedQueueSizeWarningThreshold, boundedQueueLoadFactor, waitingTask);
                queueThresholdValveOpenWindowTime = LocalDateTime.now().plusMinutes(queueThresholdValveOpenRollingWindowInMinutes);
                if (log.isDebugEnabled()) {
                    log.debug("Dynamic thread pool key for: {} open queue threshold valve", Thread.currentThread().getName());
                }
//                queueThresholdValveOpenWindowCounter.incrementAndGet();
//                queueThresholdValveOpenRollingWindowTimer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        if (queueThresholdValveOpen.get() && DynamicThreadPoolExecutor.super.getQueue().size() >= boundedQueueSizeWarningThreshold) {
//                            listener.taskThresholdTrigger(boundedQueueSizeWarningThreshold, boundedQueueLoadFactor, waitingTask);
//                        } else {
//                            queueThresholdValveOpen.compareAndSet(Boolean.TRUE, Boolean.FALSE);
//                        }
//                    }
//                }, queueThresholdValveOpenRollingWindowInMinutes);
            } else {
                if (!LocalDateTime.now().isBefore(queueThresholdValveOpenWindowTime)) {
                    listener.taskThresholdTrigger(boundedQueueSizeWarningThreshold, boundedQueueLoadFactor, waitingTask);
                    // open another window
                    queueThresholdValveOpenWindowTime = LocalDateTime.now().plusMinutes(queueThresholdValveOpenRollingWindowInMinutes);
                    if (log.isDebugEnabled()) {
                        log.debug("Dynamic thread pool key for: {} open another window of queue threshold valve", Thread.currentThread().getName());
                    }
                }
            }
        } else {
            // Closed queueThresholdValveOpen status
            if (queueThresholdValveOpen.compareAndSet(Boolean.TRUE, Boolean.FALSE)) {
                if (log.isDebugEnabled()) {
                    log.debug("Dynamic thread pool key for: {} close queue threshold valve", Thread.currentThread().getName());
                }
            }
        }
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        if (listener != null) {
            listener.beforeExecute(t, r);
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        if (t != null) {
            listener.throwableExecute(r, t);
            return;
        }
        listener.afterExecute(r);
    }

    @Override
    protected void terminated() {
        super.terminated();
    }

    public Float getBoundedQueueLoadFactor() {
        return boundedQueueLoadFactor;
    }

    public void setBoundedQueueLoadFactor(Float boundedQueueLoadFactor) {
        this.boundedQueueLoadFactor = boundedQueueLoadFactor;
    }

    public Integer getBoundedQueueSizeWarningThreshold() {
        return boundedQueueSizeWarningThreshold;
    }

    public void setBoundedQueueSizeWarningThreshold(Integer boundedQueueSizeWarningThreshold) {
        this.boundedQueueSizeWarningThreshold = boundedQueueSizeWarningThreshold;
    }

    public long getQueueThresholdValveOpenRollingWindowInMinutes() {
        return queueThresholdValveOpenRollingWindowInMinutes;
    }

    public void setQueueThresholdValveOpenRollingWindowInMinutes(long queueThresholdValveOpenRollingWindowInMinutes) {
        this.queueThresholdValveOpenRollingWindowInMinutes = queueThresholdValveOpenRollingWindowInMinutes;
    }

    protected void addListener(ExecutorListener listener) {
        this.listener = listener;
    }

    protected void removeListener() {
        this.listener = null;
    }

    /**
     * A custom reject policy class of {@link RejectedExecutionHandler} implements
     */
    public static class DynamicDiscardPolicy implements RejectedExecutionHandler {

        private String threadPoolName;

        /**
         * Creates an {@code AbortPolicy}.
         */
        public DynamicDiscardPolicy() { }

        public DynamicDiscardPolicy(String threadPoolName) {
            this.threadPoolName = threadPoolName;
        }

        /**
         * Always throws RejectedExecutionException.
         *
         * @param r the runnable task requested to be executed
         * @param e the executor attempting to execute this task
         * @throws RejectedExecutionException always
         */
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            throw new RejectedExecutionException("Task " + r.toString() + " rejected from " + e.toString());
        }
    }



}
