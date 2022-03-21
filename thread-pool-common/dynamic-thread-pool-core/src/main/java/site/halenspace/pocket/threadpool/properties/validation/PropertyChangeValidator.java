package site.halenspace.pocket.threadpool.properties.validation;

/**
 * @author Halen Leo · 2022/3/19
 */
public interface PropertyChangeValidator {
    void validate(String var1) throws ValidationException;
}
