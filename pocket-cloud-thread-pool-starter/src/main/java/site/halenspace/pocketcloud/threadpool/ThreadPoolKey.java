package site.halenspace.pocketcloud.threadpool;

/**
 * @author Halen Leo · 2021/9/5
 */
public interface ThreadPoolKey {

    /**
     * The word 'name' is used instead of 'key' so that Enums can implement this interface and it work natively.
     *
     * @return String
     */
    String name();
}
