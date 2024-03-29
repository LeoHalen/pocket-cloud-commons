package site.halenspace.pocketcloud.threadpool.javamock;

import org.springframework.core.env.Environment;
import site.halenspace.pocketcloud.threadpool.strategy.properties.ThreadPoolDynamicProperties;
import site.halenspace.pocketcloud.threadpool.strategy.properties.property.ThreadPoolDynamicProperty;

/**
 * @author Halen.Leo · 2021/9/7
 */
public class ThreadPoolPluginsMock {


    public static class ThreadPoolDynamicPropertiesMockSpringBoot implements ThreadPoolDynamicProperties {

        private Environment env;

        public void setEnvironment(Environment env) {
            this.env = env;
        }

        @Override
        public ThreadPoolDynamicProperty<String> getString(String name, String fallback) {
            return new ThreadPoolDynamicProperty<String>() {

                @Override
                public String get() {
                    return env.getProperty(name, String.class, fallback);
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
        public ThreadPoolDynamicProperty<Integer> getInteger(String name, Integer fallback) {
            return new ThreadPoolDynamicProperty<Integer>() {

                @Override
                public Integer get() {
                    return env.getProperty(name, Integer.class, fallback);
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
        public ThreadPoolDynamicProperty<Long> getLong(String name, Long fallback) {
            return new ThreadPoolDynamicProperty<Long>() {

                @Override
                public Long get() {
                    return env.getProperty(name, Long.class, fallback);
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
        public ThreadPoolDynamicProperty<Float> getFloat(String name, Float fallback) {
            return new ThreadPoolDynamicProperty<Float>() {

                @Override
                public Float get() {
                    return env.getProperty(name, Float.class, fallback);
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
        public ThreadPoolDynamicProperty<Boolean> getBoolean(String name, Boolean fallback) {
            return new ThreadPoolDynamicProperty<Boolean>() {

                @Override
                public Boolean get() {
                    return env.getProperty(name, Boolean.class, fallback);
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

}
