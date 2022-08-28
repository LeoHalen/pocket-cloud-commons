package site.halenspace.pocket.threadpool.config;

import java.util.concurrent.TimeUnit;

/**
 * @author Halen Leo · 2022/8/28
 * @blogger 后起小生
 * @github <a href="https://github.com/LeoHalen"/>
 */
public class ThreadPoolConfigDef {

    /**
     * <code>plugin.dynamic.properties.implementation</code>
     */
    public static final String PLUGIN_DYNAMIC_PROPERTIES_IMPLEMENTATION = "plugin.dynamic.properties.implementation";

    public static final String CORE_POOL_SIZE = "corePoolSize";
    public static final Integer DEFAULT_CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    public static final String MAXIMUM_POOL_SIZE = "maximumPoolSize";
    public static final Integer DEFAULT_MAXIMUM_POOL_SIZE = 2 * Runtime.getRuntime().availableProcessors();

    public static final String KEEP_ALIVE_TIME = "keepAliveTime";
    public static final Long DEFAULT_KEEP_ALIVE_TIME = 60L;
    public static final TimeUnit DEFAULT_TIMEUNIT = TimeUnit.SECONDS;
}
