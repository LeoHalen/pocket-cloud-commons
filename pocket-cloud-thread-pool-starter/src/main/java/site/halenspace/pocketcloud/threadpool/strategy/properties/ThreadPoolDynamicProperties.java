package site.halenspace.pocketcloud.threadpool.strategy.properties;

import site.halenspace.pocketcloud.threadpool.strategy.properties.property.ThreadPoolDynamicProperty;

/**
 * 提供实现动态配置的SPI接口（Service Provider Interface）
 *
 * @author Halen Leo · 2021/9/5
 */
public interface ThreadPoolDynamicProperties {

    /**
     * Requests a property that may or may not actually exist.
     * @param name property name, never <code>null</code>
     * @param fallback default value, maybe <code>null</code>
     * @return never <code>null</code>
     */
    ThreadPoolDynamicProperty<String> getString(String name, String fallback);
    /**
     * Requests a property that may or may not actually exist.
     * @param name property name, never <code>null</code>
     * @param fallback default value, maybe <code>null</code>
     * @return never <code>null</code>
     */
    ThreadPoolDynamicProperty<Integer> getInteger(String name, Integer fallback);
    /**
     * Requests a property that may or may not actually exist.
     * @param name property name, never <code>null</code>
     * @param fallback default value, maybe <code>null</code>
     * @return never <code>null</code>
     */
    ThreadPoolDynamicProperty<Long> getLong(String name, Long fallback);
    /**
     * Requests a property that may or may not actually exist.
     * @param name property name
     * @param fallback default value
     * @return never <code>null</code>
     */
    ThreadPoolDynamicProperty<Boolean> getBoolean(String name, Boolean fallback);

    class Util {
        /**
         * A convenience method to get a property by type (Class).
         * @param properties never <code>null</code>
         * @param name never <code>null</code>
         * @param fallback maybe <code>null</code>
         * @param type never <code>null</code>
         * @return a dynamic property with type T.
         */
        @SuppressWarnings("unchecked")
        public static <T> ThreadPoolDynamicProperty<T> getProperty(
                ThreadPoolDynamicProperties properties, String name, T fallback, Class<T> type) {
            return (ThreadPoolDynamicProperty<T>) doProperty(properties, name, fallback, type);
        }

        private static ThreadPoolDynamicProperty<?> doProperty(
                ThreadPoolDynamicProperties delegate, String name, Object fallback, Class<?> type) {
            if(type == String.class) {
                return delegate.getString(name, (String) fallback);
            }
            else if (type == Integer.class) {
                return delegate.getInteger(name, (Integer) fallback);
            }
            else if (type == Long.class) {
                return delegate.getLong(name, (Long) fallback);
            }
            else if (type == Boolean.class) {
                return delegate.getBoolean(name, (Boolean) fallback);
            }
            throw new IllegalStateException();
        }
    }
}
