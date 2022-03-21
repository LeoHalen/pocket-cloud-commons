package site.halenspace.pocket.threadpool.exception;

/**
 * ThreadPoolKey无法找到异常
 *
 * @author Halen.Leo · 2021/9/8
 */
public class ThreadPoolKeyNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -3579425103561867924L;

    public ThreadPoolKeyNotFoundException(String message) {
        super(message);
    }

    public ThreadPoolKeyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
