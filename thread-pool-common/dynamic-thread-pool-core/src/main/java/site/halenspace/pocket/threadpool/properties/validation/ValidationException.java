package site.halenspace.pocket.threadpool.properties.validation;

/**
 * @author Halen Leo · 2022/3/19
 */
public class ValidationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ValidationException() {
    }

    public ValidationException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public ValidationException(String arg0) {
        super(arg0);
    }

    public ValidationException(Throwable arg0) {
        super(arg0);
    }
}
