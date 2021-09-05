package site.halenspace.pocketcloud.threadpool.strategy.properties;

import site.halenspace.pocketcloud.threadpool.strategy.properties.property.ThreadPoolDynamicProperty;

/**
 * @author Halen Leo · 2021/9/5
 */
public final class ThreadPoolDynamicPropertiesSystemProperties implements ThreadPoolDynamicProperties {

    /**
     * Only public for unit test purposes.
     */
    public ThreadPoolDynamicPropertiesSystemProperties() {}

    private static class LazyHolder {
        private static final ThreadPoolDynamicProperties INSTANCE = new ThreadPoolDynamicPropertiesSystemProperties();
    }

    public static ThreadPoolDynamicProperties getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public ThreadPoolDynamicProperty<String> getString(final String name, final String fallback) {
        return new ThreadPoolDynamicProperty<String>() {

            @Override
            public String get() {
                return System.getProperty(name, fallback);
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public void addCallback(Runnable callback) {
            }
        };
    }

    @Override
    public ThreadPoolDynamicProperty<Integer> getInteger(final String name, final Integer fallback) {
        return new ThreadPoolDynamicProperty<Integer>() {

            @Override
            public Integer get() {
                return Integer.getInteger(name, fallback);
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public void addCallback(Runnable callback) {
            }
        };
    }

    @Override
    public ThreadPoolDynamicProperty<Long> getLong(final String name, final Long fallback) {
        return new ThreadPoolDynamicProperty<Long>() {

            @Override
            public Long get() {
                return Long.getLong(name, fallback);
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public void addCallback(Runnable callback) {
            }
        };
    }

    @Override
    public ThreadPoolDynamicProperty<Boolean> getBoolean(final String name, final Boolean fallback) {
        return new ThreadPoolDynamicProperty<Boolean>() {

            @Override
            public Boolean get() {
                if (System.getProperty(name) == null) {
                    return fallback;
                }
                return Boolean.getBoolean(name);
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public void addCallback(Runnable callback) {
            }
        };
    }
}
