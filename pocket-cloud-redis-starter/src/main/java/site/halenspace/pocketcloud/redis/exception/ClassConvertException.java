package site.halenspace.pocketcloud.redis.exception;

/**
 * @author Halen Leo · 2022/4/11
 */
public class ClassConvertException extends RuntimeException {
    public ClassConvertException() {
        super();
    }

    public ClassConvertException(String message) {
        super(message);
    }

    public ClassConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassConvertException(Throwable cause) {
        super(cause);
    }

    protected ClassConvertException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
