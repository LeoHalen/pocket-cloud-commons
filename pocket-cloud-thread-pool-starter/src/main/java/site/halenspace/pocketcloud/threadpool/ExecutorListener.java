package site.halenspace.pocketcloud.threadpool;

/**
 * @author Halen.Leo · 2021/9/8
 */
public interface ExecutorListener {

    void beforeExecute(Thread t, Runnable r);

    void afterExecute(Runnable r);

    void throwableExecute(Runnable r, Throwable t);
}