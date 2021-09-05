package site.halenspace.pocketcloud.threadpool.strategy.properties.property;

/**
 * @author Halen Leo · 2021/9/5
 */
public interface ThreadPoolDynamicProperty<T> extends ThreadPoolProperty<T> {

    String getName();

    /**
     * Register a callback to be run if the property is updated.
     * Backing implementations may choose to do nothing.
     * @param callback callback.
     */
    void addCallback(Runnable callback);
}
