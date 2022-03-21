package site.halenspace.pocket.threadpool.properties.property;

/**
 * @author Halen Leo · 2022/3/19
 */
public interface PropertyListener {
    void configSourceLoaded(Object var1);

    void addProperty(Object var1, String var2, Object var3, boolean var4);

    void setProperty(Object var1, String var2, Object var3, boolean var4);

    void clearProperty(Object var1, String var2, Object var3, boolean var4);

    void clear(Object var1, boolean var2);
}
