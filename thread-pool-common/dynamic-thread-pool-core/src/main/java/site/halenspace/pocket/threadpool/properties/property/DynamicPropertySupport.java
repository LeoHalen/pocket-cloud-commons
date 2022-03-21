package site.halenspace.pocket.threadpool.properties.property;

/**
 * @author Halen Leo · 2022/3/19
 */
public interface DynamicPropertySupport {
    String getString(String var1);

    void addConfigurationListener(PropertyListener var1);
}
