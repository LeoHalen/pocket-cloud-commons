package site.halenspace.pocket.threadpool.config;

import site.halenspace.pocket.threadpool.config.property.DynamicProperty;

/**
 * @author Halen Leo · 2022/8/28
 * @blogger 后起小生
 * @github <a href="https://github.com/LeoHalen"/>
 */
public interface DynamicProperties {

    /**
     * Requests a property that may or may not actually exist.
     * @param propName property name, never <code>null</code>
     * @param defValue default value, maybe <code>null</code>
     * @return maybe <code>null</code>
     */
    DynamicProperty<String> getString(String propName, String defValue);

    /**
     * Requests a property that may or may not actually exist.
     * @param propName property name, never <code>null</code>
     * @param defValue default value, maybe <code>null</code>
     * @return maybe <code>null</code>
     */
    DynamicProperty<Integer> getInteger(String propName, Integer defValue);

    /**
     * Requests a property that may or may not actually exist.
     * @param propName property name, never <code>null</code>
     * @param defValue default value, maybe <code>null</code>
     * @return maybe <code>null</code>
     */
    DynamicProperty<Long> getLong(String propName, Long defValue);

    /**
     * Requests a property that may or may not actually exist.
     * @param propName property name, never <code>null</code>
     * @param defValue default value, maybe <code>null</code>
     * @return maybe <code>null</code>
     */
    DynamicProperty<Float> getFloat(String propName, Float defValue);

    /**
     * Requests a property that may or may not actually exist.
     * @param propName property name
     * @param defValue default value
     * @return maybe <code>null</code>
     */
    DynamicProperty<Boolean> getBoolean(String propName, Boolean defValue);

    /**
     * A convenience method to get a property by type (Class).
     * @param propName property name
     * @param defValue default value
     * @param type property type (Class)
     * @return maybe <code>null</code>
     * @param <T> property type generic
     */
    <T> DynamicProperty<T> getProperty(String propName, T defValue, Class<T> type);
}
