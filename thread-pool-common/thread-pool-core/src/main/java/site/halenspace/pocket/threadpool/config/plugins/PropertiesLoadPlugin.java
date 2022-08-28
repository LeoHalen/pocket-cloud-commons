package site.halenspace.pocket.threadpool.config.plugins;

import site.halenspace.pocket.threadpool.config.DynamicProperties;
import site.halenspace.pocket.threadpool.config.DynamicPropertiesSystem;
import site.halenspace.pocket.threadpool.config.ThreadPoolConfigDef;

import java.util.ServiceLoader;

/**
 * @author Halen Leo · 2022/8/28
 * @blogger 后起小生
 * @github <a href="https://github.com/LeoHalen"/>
 */
public class PropertiesLoadPlugin {

    private final ClassLoader classLoader;

    private final DynamicProperties dynamicProperties;

    private PropertiesLoadPlugin(ClassLoader classLoader) {
        this.classLoader = classLoader;

        this.dynamicProperties = loadProperties(classLoader);
    }

    public DynamicProperties getDynamicProperties() {
        return dynamicProperties;
    }

    private DynamicProperties loadProperties(ClassLoader classLoader) {

        // Load the specified class implements properties.
        DynamicProperties dProp = loadSpecifiedClassImplementationProperties(DynamicProperties.class, classLoader);
        if (dProp !=  null) {
            return dProp;
        }

        // Load the specified path class implements properties.
        dProp = loadSpecifiedPathClassImplementationProperties(DynamicPropertiesSystem.instance());
        if (dProp != null) {
            return dProp;
        }

        // Load the default backstop policy implements system properties.
        dProp = DynamicPropertiesSystem.instance();
        return dProp;
    }

    public static <T> T loadSpecifiedClassImplementationProperties(Class<T> clazz, ClassLoader classLoader) {
        assert clazz != null;
        assert classLoader != null;
        ServiceLoader<T> serviceLoader = ServiceLoader.load(clazz, classLoader);
        for (T s : serviceLoader) {
            if (s != null) {
                return s;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T loadSpecifiedPathClassImplementationProperties(DynamicProperties dynamicProperties) {
        // Special path class implementation config definition.
        String implementingClass = dynamicProperties.getString(ThreadPoolConfigDef.PLUGIN_DYNAMIC_PROPERTIES_IMPLEMENTATION, null).get();

        if (implementingClass == null) {
            return null;
        }

        try {
            Class<?> cls = Class.forName(implementingClass);
            return (T) cls.newInstance();
        } catch (ClassCastException e) {
            throw new RuntimeException("'DynamicProperties.class' implementation is not an instance of 'DynamicProperties.class': " + implementingClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("'DynamicProperties.class' implementation class not found: " + implementingClass, e);
        } catch (InstantiationException e) {
            throw new RuntimeException("'DynamicProperties.class' implementation not able to be instantiated: " + implementingClass, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("'DynamicProperties.class' implementation not able to be accessed: " + implementingClass, e);
        }
    }

    public static PropertiesLoadPlugin instance() {
        return PropertiesLoadPlugin.LazyHolder.instance;
    }

    private static class LazyHolder {
        private static final PropertiesLoadPlugin instance = new PropertiesLoadPlugin(PropertiesLoadPlugin.class.getClassLoader());
    }
}