package site.halenspace.pocketcloud.threadpool;

/**
 * Default implementation of the interface
 *
 * @author Halen Leo · 2021/9/5
 */
public abstract class ThreadPoolKeyDefault implements ThreadPoolKey {

    private final String name;

    public ThreadPoolKeyDefault(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
