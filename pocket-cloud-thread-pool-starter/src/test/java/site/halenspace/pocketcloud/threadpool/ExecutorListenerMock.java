package site.halenspace.pocketcloud.threadpool;

import java.util.concurrent.ExecutorService;

/**
 * @author Halen.Leo · 2021/9/8
 */
public class ExecutorListenerMock {

    public static void main(String[] args) {
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
    }
}
