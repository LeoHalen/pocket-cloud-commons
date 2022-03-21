package site.halenspace.pocket.threadpool.properties;

import site.halenspace.pocket.threadpool.properties.property.DynamicProperty;

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
    public DynamicProperty<String> getString(final String name, final String fallback) {
        return new DynamicProperty<String>() {

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
    public DynamicProperty<Integer> getInteger(final String name, final Integer fallback) {
        return new DynamicProperty<Integer>() {

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
    public DynamicProperty<Long> getLong(final String name, final Long fallback) {
        return new DynamicProperty<Long>() {

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
    public DynamicProperty<Float> getFloat(String name, Float fallback) {
        return new DynamicProperty<Float>() {

            @Override
            public Float get() {
                String v;
                if ((v = System.getProperty(name)) == null) {
                    return fallback;
                }
                return Float.parseFloat(v);
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
    public DynamicProperty<Boolean> getBoolean(final String name, final Boolean fallback) {
        return new DynamicProperty<Boolean>() {

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
