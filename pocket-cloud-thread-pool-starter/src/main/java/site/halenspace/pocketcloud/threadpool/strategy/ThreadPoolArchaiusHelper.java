package site.halenspace.pocketcloud.threadpool.strategy;

import site.halenspace.pocketcloud.threadpool.strategy.properties.ThreadPoolDynamicProperties;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Halen Leo · 2021/9/5
 */
public class ThreadPoolArchaiusHelper {

    /**
     * To keep class loading minimal for those that have archaius in the classpath but choose not to use it.
     * @ExcludeFromJavadoc
     * @author agent
     */
    private static class LazyHolder {
        private final static Method loadCascadedPropertiesFromResources;
        private final static String CONFIG_MANAGER_CLASS = "com.netflix.config.ConfigurationManager";

        static {
            Method load = null;
            try {
                Class<?> configManager = Class.forName(CONFIG_MANAGER_CLASS);
                load = configManager.getMethod("loadCascadedPropertiesFromResources", String.class);
            } catch (Exception ignored) {
            }

            loadCascadedPropertiesFromResources = load;
        }
    }

    static boolean isArchaiusV1Available() {
        return LazyHolder.loadCascadedPropertiesFromResources != null;
    }

    static void loadCascadedPropertiesFromResources(String name) {
        if (isArchaiusV1Available()) {
            try {
                LazyHolder.loadCascadedPropertiesFromResources.invoke(null, name);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ignored) {
            }
        }
    }

    static ThreadPoolDynamicProperties createArchaiusDynamicProperties() {
        if (isArchaiusV1Available()) {
            loadCascadedPropertiesFromResources("dynamic-thread-pool-plugins");
            try {
                Class<?> defaultProperties = Class.forName(
                        "com.netflix.hystrix.strategy.properties.archaius" + ".HystrixDynamicPropertiesArchaius");
                return (ThreadPoolDynamicProperties) defaultProperties.newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        // Fallback to System properties.
        return null;
    }
}
