package site.halenspace.pocket.threadpool.config.property;

/**
 * @author Halen Leo · 2022/8/28
 * @blogger 后起小生
 * @github <a href="https://github.com/LeoHalen"/>
 */
public interface DynamicProperty<T> extends Property<T> {

    /**
     * Get the property name.
     * @return property name.
     */
    String getPropName();

    /**
     * Register a callback to be run if the property is updated.
     * Backing implementations may choose to do nothing.
     * @param callback callback.
     */
    void addCallback(Runnable callback);
}
