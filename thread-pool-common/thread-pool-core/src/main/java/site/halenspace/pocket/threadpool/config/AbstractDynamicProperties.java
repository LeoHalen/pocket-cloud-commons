package site.halenspace.pocket.threadpool.config;

import site.halenspace.pocket.threadpool.config.property.DynamicProperty;

/**
 * @author Halen Leo · 2022/8/28
 * @blogger 后起小生
 * @github <a href="https://github.com/LeoHalen"/>
 */
public abstract class AbstractDynamicProperties implements DynamicProperties {

    /**
     * A convenience method to get a property by type (Class).
     * @param propName never <code>null</code>
     * @param defValue maybe <code>null</code>
     * @param type never <code>null</code>
     * @return a dynamic property with type T.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> DynamicProperty<T> getProperty(String propName, T defValue, Class<T> type) {
        return (DynamicProperty<T>) doProperty(propName, defValue, type);
    }

    private DynamicProperty<?> doProperty(String propName, Object defValue, Class<?> type) {
        if (String.class.equals(type)) {
            return getString(propName, (String) defValue);
        }
        else if (Integer.class.equals(type)) {
            return getInteger(propName, (Integer) defValue);
        }
        else if (Long.class.equals(type)) {
            return getLong(propName, (Long) defValue);
        }
        else if (Float.class.equals(type)) {
            return getFloat(propName, (Float) defValue);
        }
        else if (Boolean.class.equals(type)) {
            return getBoolean(propName, (Boolean) defValue);
        }
        throw new IllegalStateException();
    }
}
