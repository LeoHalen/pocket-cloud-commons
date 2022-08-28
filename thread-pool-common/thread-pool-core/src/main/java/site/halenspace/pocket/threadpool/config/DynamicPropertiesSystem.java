package site.halenspace.pocket.threadpool.config;

import site.halenspace.pocket.threadpool.config.property.DynamicProperty;

/**
 * Achieve dynamic acquisition of system properties
 *
 * @author Halen Leo · 2022/8/28
 * @blogger 后起小生
 * @github <a href="https://github.com/LeoHalen"/>
 */
public class DynamicPropertiesSystem extends AbstractDynamicProperties {

    private DynamicPropertiesSystem() {}

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
                return System.getProperty(propName, defValue);
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
                return Integer.getInteger(propName, defValue);
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
                return Long.getLong(propName, defValue);
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
                String val;
                if ((val = System.getProperty(propName)) == null) {
                    return defValue;
                }
                return Float.parseFloat(val);
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
                if (System.getProperty(propName) == null) {
                    return defValue;
                }
                return Boolean.getBoolean(propName);
            }
        };
    }

    public static DynamicPropertiesSystem instance() {
        return LazyHolder.instance;
    }

    private static class LazyHolder {
        private static final DynamicPropertiesSystem instance = new DynamicPropertiesSystem();
    }
}
