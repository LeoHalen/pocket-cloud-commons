package site.halenspace.pocket.threadpool.config;

import site.halenspace.pocket.threadpool.config.property.DynamicProperty;

/**
 * For testing only
 *
 * @author Halen Leo · 2022/8/28
 * @blogger 后起小生
 * @github <a href="https://github.com/LeoHalen"/>
 */
public class DynamicPropertiesSystemTestServiceLoaderOne extends AbstractDynamicProperties {

    /**
     * Testing {@link java.util.ServiceLoader} must use public methods
     */
    public DynamicPropertiesSystemTestServiceLoaderOne() {}

    @Override
    public DynamicProperty<String> getString(String propName, String defValue) {
        return new DynamicProperty<String>() {
            @Override
            public String getPropName() {
                return propName;
            }

            @Override
            public void addCallback(Runnable callback) {
                callback.run();
            }

            @Override
            public String get() {
                return "100";
            }
        };
    }

    @Override
    public DynamicProperty<Integer> getInteger(String propName, Integer defValue) {
        return new DynamicProperty<Integer>() {
            @Override
            public String getPropName() {
                return propName;
            }

            @Override
            public void addCallback(Runnable callback) {
                callback.run();
            }

            @Override
            public Integer get() {
                return 100;
            }
        };
    }

    @Override
    public DynamicProperty<Long> getLong(String propName, Long defValue) {
        return new DynamicProperty<Long>() {
            @Override
            public String getPropName() {
                return propName;
            }

            @Override
            public void addCallback(Runnable callback) {
                callback.run();
            }

            @Override
            public Long get() {
                return 100L;
            }
        };
    }

    @Override
    public DynamicProperty<Float> getFloat(String propName, Float defValue) {
        return new DynamicProperty<Float>() {
            @Override
            public String getPropName() {
                return propName;
            }

            @Override
            public void addCallback(Runnable callback) {
                callback.run();
            }

            @Override
            public Float get() {
                return 100.00F;
            }
        };
    }

    @Override
    public DynamicProperty<Boolean> getBoolean(String propName, Boolean defValue) {
        return new DynamicProperty<Boolean>() {
            @Override
            public String getPropName() {
                return propName;
            }

            @Override
            public void addCallback(Runnable callback) {
                callback.run();
            }

            @Override
            public Boolean get() {
                return Boolean.TRUE;
            }
        };
    }
}