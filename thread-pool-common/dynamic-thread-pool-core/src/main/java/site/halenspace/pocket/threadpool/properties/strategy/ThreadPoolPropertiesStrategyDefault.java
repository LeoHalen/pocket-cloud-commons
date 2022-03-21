package site.halenspace.pocket.threadpool.properties.strategy;

/**
 * @author Halen Leo · 2021/9/6
 */
public class ThreadPoolPropertiesStrategyDefault extends ThreadPoolPropertiesStrategy {

    private final static ThreadPoolPropertiesStrategyDefault INSTANCE = new ThreadPoolPropertiesStrategyDefault();

    private ThreadPoolPropertiesStrategyDefault() {}

    public static ThreadPoolPropertiesStrategy getInstance() {
        return INSTANCE;
    }
}
