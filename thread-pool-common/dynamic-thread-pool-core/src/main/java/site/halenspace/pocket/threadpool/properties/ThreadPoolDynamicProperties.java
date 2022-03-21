package site.halenspace.pocket.threadpool.properties;

import site.halenspace.pocket.threadpool.properties.property.DynamicProperty;

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
    DynamicProperty<String> getString(String name, String fallback);
    /**
     * Requests a property that may or may not actually exist.
     * @param name property name, never <code>null</code>
     * @param fallback default value, maybe <code>null</code>
     * @return never <code>null</code>
     */
    DynamicProperty<Integer> getInteger(String name, Integer fallback);
    /**
     * Requests a property that may or may not actually exist.
     * @param name property name, never <code>null</code>
     * @param fallback default value, maybe <code>null</code>
     * @return never <code>null</code>
     */
    DynamicProperty<Long> getLong(String name, Long fallback);
    /**
     * Requests a property that may or may not actually exist.
     * @param name property name, never <code>null</code>
     * @param fallback default value, maybe <code>null</code>
     * @return never <code>null</code>
     */
    DynamicProperty<Float> getFloat(String name, Float fallback);
    /**
     * Requests a property that may or may not actually exist.
     * @param name property name
     * @param fallback default value
     * @return never <code>null</code>
     */
    DynamicProperty<Boolean> getBoolean(String name, Boolean fallback);

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
        public static <T> DynamicProperty<T> getProperty(
                ThreadPoolDynamicProperties properties, String name, T fallback, Class<T> type) {
            return (DynamicProperty<T>) doProperty(properties, name, fallback, type);
        }

        private static DynamicProperty<?> doProperty(
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
            else if (type == Float.class) {
                return delegate.getFloat(name, (Float) fallback);
            }
            else if (type == Boolean.class) {
                return delegate.getBoolean(name, (Boolean) fallback);
            }
            throw new IllegalStateException();
        }
    }
}
