package site.halenspace.pocket.threadpool.exception;

/**
 * 线程池执行器创建失败异常
 *
 * @author Halen Leo · 2021/7/4
 */
public class ExecutorCreateFailureException extends RuntimeException {

    private static final long serialVersionUID = -8341452103561805856L;

    public ExecutorCreateFailureException(String message) {
        super(message);
    }

    public ExecutorCreateFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
