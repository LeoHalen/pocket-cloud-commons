package site.halenspace.pocket.threadpool.properties.property;

/**
 * @author Halen Leo · 2021/9/5
 */
public interface DynamicProperty<T> extends Property<T> {

    String getName();

    /**
     * Register a callback to be run if the property is updated.
     * Backing implementations may choose to do nothing.
     * @param callback callback.
     */
    void addCallback(Runnable callback);
}
