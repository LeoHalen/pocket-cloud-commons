package site.halenspace.pocketcloud.threadpool.strategy;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import site.halenspace.pocketcloud.threadpool.strategy.properties.ThreadPoolDynamicProperties;
import site.halenspace.pocketcloud.threadpool.strategy.properties.ThreadPoolDynamicPropertiesSystemProperties;
import site.halenspace.pocketcloud.threadpool.strategy.properties.ThreadPoolPropertiesStrategy;
import site.halenspace.pocketcloud.threadpool.strategy.properties.ThreadPoolPropertiesStrategyDefault;
import site.halenspace.pocketcloud.threadpool.util.SpringContextUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Halen Leo · 2021/9/5
 */
public class ThreadPoolPlugins {

    private static class LazyHolder { private static final ThreadPoolPlugins INSTANCE = ThreadPoolPlugins.create(); }
    private final ClassLoader classLoader;
    /* package */ final AtomicReference<ThreadPoolPropertiesStrategy> propertiesFactory = new AtomicReference<>();
    private final ThreadPoolDynamicProperties dynamicProperties;

    private ThreadPoolPlugins(ClassLoader classLoader) {
        //This will load Archaius if its in the classpath.
        this.classLoader = classLoader;
        //N.B. Do not use a logger before this is loaded as it will most likely load the configuration system.
        //The configuration system may need to do something prior to loading logging. @agentgt
        dynamicProperties = resolveDynamicProperties(classLoader);
    }

    /**
     * For unit test purposes.
     * @ExcludeFromJavadoc
     */
    /* private */ static ThreadPoolPlugins create(ClassLoader classLoader) {
        return new ThreadPoolPlugins(classLoader);
    }

    /**
     * For unit test purposes.
     * @ExcludeFromJavadoc
     */
//     private  static ThreadPoolPlugins create(ClassLoader classLoader) {
//        return new createThreadPoolPlugins(classLoader);
//     }

    /* private */ static ThreadPoolPlugins create() {
        return create(ThreadPoolPlugins.class.getClassLoader());
    }

    public static ThreadPoolPlugins getInstance() {
        return LazyHolder.INSTANCE;
    }

    /**
     * Reset all of the HystrixPlugins to null.  You may invoke this directly, or it also gets invoked via <code>Hystrix.reset()</code>
     */
    public static void reset() {
        getInstance().propertiesFactory.set(null);
    }

    /**
     * Retrieve instance of {@link ThreadPoolPropertiesStrategy} to use based on order of precedence as defined in {@link ThreadPoolPlugins} class header.
     * <p>
     * Override default by using {@link #registerPropertiesStrategy(ThreadPoolPropertiesStrategy)} or setting property (via Archaius): <code>hystrix.plugin.HystrixPropertiesStrategy.implementation</code> with the full
     * classname to load.
     *
     * @return {@link ThreadPoolPropertiesStrategy} implementation to use
     */
    public ThreadPoolPropertiesStrategy getPropertiesStrategy() {
        if (propertiesFactory.get() == null) {
            // check for an implementation from Archaius first
            ThreadPoolPropertiesStrategy impl = getPluginImplementation(ThreadPoolPropertiesStrategy.class);
            if (impl == null) {
                // nothing set via Archaius so initialize with default
                propertiesFactory.compareAndSet(null, ThreadPoolPropertiesStrategyDefault.getInstance());
                // we don't return from here but call get() again in case of thread-race so the winner will always get returned
            } else {
                // we received an implementation from Archaius so use it
                propertiesFactory.compareAndSet(null, impl);
            }
        }
        return propertiesFactory.get();
    }

    /**
     * 获取动态配置
     * @return
     */
    public ThreadPoolDynamicProperties getDynamicProperties() {
        return dynamicProperties;
    }

    /**
     * Register a {@link ThreadPoolPropertiesStrategy} implementation as a global override of any injected or default implementations.
     *
     * @param impl
     *            {@link ThreadPoolPropertiesStrategy} implementation
     * @throws IllegalStateException
     *             if called more than once or after the default was initialized (if usage occurs before trying to register)
     */
    public void registerPropertiesStrategy(ThreadPoolPropertiesStrategy impl) {
        if (!propertiesFactory.compareAndSet(null, impl)) {
            throw new IllegalStateException("Another strategy was already registered.");
        }
    }

    private <T> T getPluginImplementation(Class<T> pluginClass) {
        T p = getPluginImplementationViaProperties(pluginClass, dynamicProperties);
        if (p != null) return p;
        return findService(pluginClass, classLoader);
    }

    @SuppressWarnings("unchecked")
    private static <T> T getPluginImplementationViaProperties(Class<T> pluginClass, ThreadPoolDynamicProperties dynamicProperties) {
        String classSimpleName = pluginClass.getSimpleName();
        // Check Archaius for plugin class.
        String propertyName = "thread.pool.plugin." + classSimpleName + ".implementation";
        String implementingClass = dynamicProperties.getString(propertyName, null).get();
        if (implementingClass != null) {
            try {
                Class<?> cls = Class.forName(implementingClass);
                // narrow the scope (cast) to the type we're expecting
                cls = cls.asSubclass(pluginClass);
                return (T) cls.newInstance();
            } catch (ClassCastException e) {
                throw new RuntimeException(classSimpleName + " implementation is not an instance of " + classSimpleName + ": " + implementingClass);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(classSimpleName + " implementation class not found: " + implementingClass, e);
            } catch (InstantiationException e) {
                throw new RuntimeException(classSimpleName + " implementation not able to be instantiated: " + implementingClass, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(classSimpleName + " implementation not able to be accessed: " + implementingClass, e);
            }
        } else {
            return null;
        }
    }

    private static ThreadPoolDynamicProperties resolveDynamicProperties(ClassLoader classLoader) {
        ThreadPoolDynamicProperties hp = getPluginImplementationViaProperties(
                ThreadPoolDynamicProperties.class, ThreadPoolDynamicPropertiesSystemProperties.getInstance());
        if (hp != null) {
            Environment environment = SpringContextUtil.applicationContext.getEnvironment();
            try {
                Method setEnvironment = hp.getClass().getDeclaredMethod("setEnvironment", Environment.class);
                setEnvironment.invoke(hp, environment);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            return hp;
        }
        // 外部自定义扩展的方式
        hp = findService(ThreadPoolDynamicProperties.class, classLoader);
        if (hp != null) {
            return hp;
        }
        // archaius方式
//        hp = ThreadPoolArchaiusHelper.createArchaiusDynamicProperties();
//        if (hp != null) {
//            return hp;
//        }
        // 系统配置属性动态加载方式托底
        hp = ThreadPoolDynamicPropertiesSystemProperties.getInstance();
        return hp;
    }

    private static <T> T findService(Class<T> spi, ClassLoader classLoader) throws ServiceConfigurationError {

        ServiceLoader<T> sl = ServiceLoader.load(spi, classLoader);
        for (T s : sl) {
            if (s != null)
                return s;
        }
        return null;
    }
}
