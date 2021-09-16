package site.halenspace.pocketcloud.threadpool.javamock;

import site.halenspace.pocketcloud.threadpool.*;
import site.halenspace.pocketcloud.threadpool.consts.QueueTypeConst;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @author Halen.Leo · 2021/9/8
 */
public class ExecutorListenerMock {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
//        mockNotAllThreadsAfterExecute();
//        mockNoneOfTheThreadsAfterExecute();
//        mockAllThreadsAfterExecuteSuccess();
//        mockNoneOfTheThreadsThrowableExecute();
//        mockTheThreadsThrowableExecuteSuccess();
//        mockSubmitHandlerThrowableExecuteSuccess();
//        mockQueueTaskThresholdTrigger();
        mockQueueTaskCustomThresholdTrigger();
    }

    /**
     * mock使用过程中需要的注意事项 -> 重写Listener的afterExecute, 但是并没有所有线程执行完毕都回调此方法
     */
    public static void mockNotAllThreadsAfterExecute() {
        DynamicThreadPoolKey threadPoolKey = DynamicThreadPoolKey.Factory.asKey("testExecutor");
        ExecutorService executorService = DynamicThreadPoolManager.getInstance().getExecutorOrCreateDefault(threadPoolKey);
        MockExecutorListener listener = new MockExecutorListener();
        DynamicThreadPoolManager.getInstance().addExecutorListener(threadPoolKey, listener);

        int i = 10;
        while (i-- > 0) {
            executorService.execute(() -> {
                System.out.println(threadPoolKey + "线程池实例 | 当前线程名: " + Thread.currentThread().getName());
            });
        }
    }

    /**
     * mock使用过程中需要的注意事项 -> 重写Listener的afterExecute, 所有线程完毕都没有回调此方法
     */
    public static void mockNoneOfTheThreadsAfterExecute() {
        DynamicThreadPoolKey threadPoolKey = DynamicThreadPoolKey.Factory.asKey("testExecutor");
        ExecutorService executorService = DynamicThreadPoolManager.getInstance().getExecutorOrCreateDefault(threadPoolKey);
        MockExecutorListener listener = new MockExecutorListener();
        DynamicThreadPoolManager.getInstance().addExecutorListener(threadPoolKey, listener);

        int i = 10;
        while (i-- > 0) {
            executorService.execute(() -> {
                System.out.println(threadPoolKey + "线程池实例 | 当前线程名: " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * mock使用过程中需要的注意事项 -> 重写Listener的afterExecute, 所有线程均成功执行回调此方法
     */
    public static void mockAllThreadsAfterExecuteSuccess() throws InterruptedException {
        DynamicThreadPoolKey threadPoolKey = DynamicThreadPoolKey.Factory.asKey("testExecutor");
        ExecutorService executorService = DynamicThreadPoolManager.getInstance().getExecutorOrCreateDefault(threadPoolKey);
        MockExecutorListener listener = new MockExecutorListener();
        DynamicThreadPoolManager.getInstance().addExecutorListener(threadPoolKey, listener);

        int i = 10;
        while (i-- > 0) {
            executorService.execute(() -> {
                System.out.println(threadPoolKey + "线程池实例 | 当前线程名: " + Thread.currentThread().getName());
            });
        }
        Thread.sleep(2000);
    }

    /**
     * mock使用过程中需要的注意事项 -> 重写Listener的throwableExecute, 线程只回调了此方法但并未抛出异常
     *  note: ExecutorService.submit方法底层将异常捕获并存放到 {@link FutureTask} 中返回
     */
    public static void mockNoneOfTheThreadsThrowableExecute() throws InterruptedException {
        DynamicThreadPoolKey threadPoolKey = DynamicThreadPoolKey.Factory.asKey("testExecutor");
        ExecutorService executorService = DynamicThreadPoolManager.getInstance().getExecutorOrCreateDefault(threadPoolKey);
        MockExecutorListener listener = new MockExecutorListener();
        DynamicThreadPoolManager.getInstance().addExecutorListener(threadPoolKey, listener);

        int i = 10;
        while (i-- > 0) {
            executorService.submit(() -> {
                System.out.println(threadPoolKey + "线程池实例 | 当前线程名: " + Thread.currentThread().getName());
                int c = 10 / 0;
            });
        }
        Thread.sleep(2000);
    }

    /**
     * mock使用过程中需要的注意事项 -> 重写Listener的throwableExecute, 线程成功回调此方法并且也抛出了异常
     *  note: ExecutorService.execute方法底层并没有吞掉异常
     */
    public static void mockExecuteHandlerThrowableExecuteSuccess() throws InterruptedException {
        DynamicThreadPoolKey threadPoolKey = DynamicThreadPoolKey.Factory.asKey("testExecutor");
        ExecutorService executorService = DynamicThreadPoolManager.getInstance().getExecutorOrCreateDefault(threadPoolKey);
        MockExecutorListener listener = new MockExecutorListener();
        DynamicThreadPoolManager.getInstance().addExecutorListener(threadPoolKey, listener);

        int i = 10;
        while (i-- > 0) {
            executorService.execute(() -> {
                System.out.println(threadPoolKey + "线程池实例 | 当前线程名: " + Thread.currentThread().getName());
                int c = 10 / 0;
            });
        }
        Thread.sleep(2000);
    }

    /**
     * mock使用过程中需要的注意事项 -> 重写Listener的throwableExecute, 线程成功回调此方法并且也获取到了异常
     *  note: ExecutorService.submit方法底层将异常捕获并存放到 {@link FutureTask} 中返回
     */
    public static void mockSubmitHandlerThrowableExecuteSuccess() throws InterruptedException, ExecutionException {
        DynamicThreadPoolKey threadPoolKey = DynamicThreadPoolKey.Factory.asKey("testExecutor");
        ExecutorService executorService = DynamicThreadPoolManager.getInstance().getExecutorOrCreateDefault(threadPoolKey);
        MockExecutorListener listener = new MockExecutorListener();
        DynamicThreadPoolManager.getInstance().addExecutorListener(threadPoolKey, listener);

        int i = 10;
        while (i-- > 0) {
            Future<?> resultFuture = executorService.submit(() -> {
                System.out.println(threadPoolKey + "线程池实例 | 当前线程名: " + Thread.currentThread().getName());
                int c = 10 / 0;
            });
            resultFuture.get();
        }
        Thread.sleep(2000);
    }

    /**
     * mock使用过程中需要的注意事项 -> 重写Listener的taskThresholdTrigger, 队列任务达到队列最大长度线程成功回调此方法
     *  note: 如果使用submit方法并在主线程中get阻塞等待，则无法mock此功能
     */
    public static void mockQueueTaskDefaultThresholdTrigger() throws InterruptedException, ExecutionException {
        System.setProperty("dynamic.threadpool.testExecutor.corePoolSize", "2");
        System.setProperty("dynamic.threadpool.testExecutor.maximumPoolSize", "2");
        System.setProperty("dynamic.threadpool.testExecutor.queueType", QueueTypeConst.LinkedBlockingQueue);
        System.setProperty("dynamic.threadpool.testExecutor.maxQueueSize", "10");

        DynamicThreadPoolKey threadPoolKey = DynamicThreadPoolKey.Factory.asKey("testExecutor");
        DynamicThreadPoolExecutor executorService = (DynamicThreadPoolExecutor) DynamicThreadPoolManager.getInstance().getExecutorOrCreateDefault(threadPoolKey);
        MockExecutorListener listener = new MockExecutorListener();
        DynamicThreadPoolManager.getInstance().addExecutorListener(threadPoolKey, listener);

        int i = 12;
        while (i-- > 0) {
            executorService.execute(() -> {
                System.out.println(threadPoolKey + "线程池实例 | 当前线程名: " + Thread.currentThread().getName());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        Thread.sleep(10000);
    }

    /**
     * mock使用过程中需要的注意事项 -> 重写Listener的taskThresholdTrigger, 队列任务达到自定义阈值线程成功回调此方法
     *  note: 如果使用submit方法并在主线程中get阻塞等待，则无法mock此功能
     */
    public static void mockQueueTaskCustomThresholdTrigger() throws InterruptedException, ExecutionException {
        System.setProperty("dynamic.threadpool.testExecutor.corePoolSize", "2");
        System.setProperty("dynamic.threadpool.testExecutor.maximumPoolSize", "2");
        System.setProperty("dynamic.threadpool.testExecutor.queueType", QueueTypeConst.LinkedBlockingQueue);
        System.setProperty("dynamic.threadpool.testExecutor.maxQueueSize", "10");
        System.setProperty("dynamic.threadpool.testExecutor.queueWarningLoadFactor", "0.8");
        System.setProperty("dynamic.threadpool.testExecutor.queueThresholdValveOpenRollingWindowInMinutes", "1");

        DynamicThreadPoolKey threadPoolKey = DynamicThreadPoolKey.Factory.asKey("testExecutor");
        DynamicThreadPoolExecutor executorService = (DynamicThreadPoolExecutor) DynamicThreadPoolManager.getInstance().getExecutorOrCreateDefault(threadPoolKey);
        MockExecutorListener listener = new MockExecutorListener();
        DynamicThreadPoolManager.getInstance().addExecutorListener(threadPoolKey, listener);

        int i = 10;
        while (i-- > 0) {
            executorService.execute(() -> {
                System.out.println(threadPoolKey + "线程池实例 | 当前线程名: " + Thread.currentThread().getName());
                try {
                    Thread.sleep(70000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        Thread.sleep(60000);
        i = 2;
        while (i-- > 0) {
            executorService.execute(() -> {
                System.out.println(threadPoolKey + "线程池实例 | 当前线程名: " + Thread.currentThread().getName());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        Thread.sleep(80000);
    }

    /**
     * Mock interface {@link ExecutorListener} implements
     */
    public static class MockExecutorListener implements ExecutorListener {

        @Override
        public void beforeExecute(Thread t, Runnable r) {
            System.out.println("线程执行前 | 当前线程名: " + t.getName() + "Runnable: " +r.toString());
        }

        @Override
        public void afterExecute(Runnable r) {
            System.out.println("线程执行后 | Runnable: " + r.toString());
        }

        @Override
        public void throwableExecute(Runnable r, Throwable t) {
            System.out.println("线程执行后 | Runnable: " + r.toString() + "异常: " + t.toString());
        }

        @Override
        public void taskThresholdTrigger(int threshold, float loadFactor, int waitingTask) {
            System.out.println("队列负载触发阈值 | threshold: " + threshold + ", loadFactor: " + loadFactor + ", waitingTask: " + waitingTask);
        }
    }
}
