package site.halenspace.pocketcloud.threadpool.exeception;

/**
 * 线程池执行器刷新失败异常
 *
 * @author Halen Leo · 2021/8/10
 */
public class ExecutorRefreshFailureException extends RuntimeException {

    private static final long serialVersionUID = -8341452103561805856L;

    public ExecutorRefreshFailureException(String message) {
        super(message);
    }

    public ExecutorRefreshFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
