package site.halenspace.pocket.threadpool.test.config;

import org.junit.Test;
import site.halenspace.pocket.threadpool.config.DynamicPropertiesSystem;
import site.halenspace.pocket.threadpool.config.ThreadPoolConfigDef;

import static org.junit.Assert.assertEquals;

/**
 * @author Zg.Li · 2022/8/10
 */
public class DynamicPropertiesSystemTest {

    private final DynamicPropertiesSystem properties = DynamicPropertiesSystem.instance();

    static {
        System.setProperty(getPropName(ThreadPoolConfigDef.CORE_POOL_SIZE), "1");
        System.setProperty(getPropName(ThreadPoolConfigDef.MAXIMUM_POOL_SIZE), "2");
        System.setProperty(getPropName(ThreadPoolConfigDef.KEEP_ALIVE_TIME), "30");
    }

    private static String getPropName(String name) {
        return "dynamic.thread-pool.test-pool." + name;
    }

    @Test
    public void testSystemPropertyAcquisition() {

        assertEquals(new Integer(1), properties.getProperty(getPropName(ThreadPoolConfigDef.CORE_POOL_SIZE), null, Integer.class).get());
        assertEquals(new Integer(2), properties.getProperty(getPropName(ThreadPoolConfigDef.MAXIMUM_POOL_SIZE), null, Integer.class).get());
        assertEquals(new Long(30), properties.getProperty(getPropName(ThreadPoolConfigDef.KEEP_ALIVE_TIME), null, Long.class).get());
    }

    @Test
    public void testSystemPropertyAcquisitionAfterChange() {

        assertEquals(new Integer(1), properties.getProperty(getPropName(ThreadPoolConfigDef.CORE_POOL_SIZE), null, Integer.class).get());
        assertEquals(new Integer(2), properties.getProperty(getPropName(ThreadPoolConfigDef.MAXIMUM_POOL_SIZE), null, Integer.class).get());
        assertEquals(new Long(30), properties.getProperty(getPropName(ThreadPoolConfigDef.KEEP_ALIVE_TIME), null, Long.class).get());

        System.setProperty(getPropName(ThreadPoolConfigDef.CORE_POOL_SIZE), "2");
        System.setProperty(getPropName(ThreadPoolConfigDef.MAXIMUM_POOL_SIZE), "3");
        System.setProperty(getPropName(ThreadPoolConfigDef.KEEP_ALIVE_TIME), "1500");

        assertEquals(new Integer(2), properties.getProperty(getPropName(ThreadPoolConfigDef.CORE_POOL_SIZE), null, Integer.class).get());
        assertEquals(new Integer(3), properties.getProperty(getPropName(ThreadPoolConfigDef.MAXIMUM_POOL_SIZE), null, Integer.class).get());
        assertEquals(new Long(1500), properties.getProperty(getPropName(ThreadPoolConfigDef.KEEP_ALIVE_TIME), null, Long.class).get());

    }
}
