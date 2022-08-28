package site.halenspace.pocket.threadpool.test.plugins;

import org.junit.Test;
import site.halenspace.pocket.threadpool.config.*;
import site.halenspace.pocket.threadpool.config.plugins.PropertiesLoadPlugin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Zg.Li · 2022/8/11
 */
public class PropertiesLoadPluginTest {

    private static String getPropName(String name) {
        return "dynamic.thread-pool.test-pool." + name;
    }

    @Test
    public void testLoadSpecifiedClassImplementationProperties() {
        DynamicProperties properties = PropertiesLoadPlugin.loadSpecifiedClassImplementationProperties(DynamicProperties.class, PropertiesLoadPlugin.class.getClassLoader());

        assertTrue(properties instanceof DynamicPropertiesSystemTestServiceLoaderOne);
        assertEquals("100", properties.getProperty(getPropName("testPropName"), null, String.class).get());
        assertEquals(new Integer(100), properties.getProperty(getPropName("testPropName"), null, Integer.class).get());
        assertEquals(new Long(100L), properties.getProperty(getPropName("testPropName"), null, Long.class).get());
        assertEquals(new Float(100.00F), properties.getProperty(getPropName("testPropName"), null, Float.class).get());
        assertEquals(Boolean.TRUE, properties.getProperty(getPropName("testPropName"), null, Boolean.class).get());

    }

    @Test
    public void testLoadSpecifiedPathClassImplementationProperties() {
        System.setProperty(ThreadPoolConfigDef.PLUGIN_DYNAMIC_PROPERTIES_IMPLEMENTATION, "site.halenspace.pocket.threadpool.config.DynamicPropertiesSystemTestServiceLoaderTwo");
        DynamicProperties properties = PropertiesLoadPlugin.loadSpecifiedPathClassImplementationProperties(DynamicPropertiesSystem.instance());

        assertTrue(properties instanceof DynamicPropertiesSystemTestServiceLoaderTwo);
        assertEquals("200", properties.getProperty(getPropName("testPropName"), null, String.class).get());
        assertEquals(new Integer(200), properties.getProperty(getPropName("testPropName"), null, Integer.class).get());
        assertEquals(new Long(200L), properties.getProperty(getPropName("testPropName"), null, Long.class).get());
        assertEquals(new Float(200.00F), properties.getProperty(getPropName("testPropName"), null, Float.class).get());
        assertEquals(Boolean.FALSE, properties.getProperty(getPropName("testPropName"), null, Boolean.class).get());

    }

    /**
     * Need to test on the premise of a specified
     * path file 'meta-info/service/site.halenspace.pocket.threadpool.config.DynamicProperties' configuration
     * class full path {@link DynamicPropertiesSystemTestServiceLoaderOne}.
     */
    @Test
    public void testLoadPropertiesPriorityAssertLoadFirstPolicy() {
        DynamicProperties properties = PropertiesLoadPlugin.instance().getDynamicProperties();

        assertTrue(properties instanceof DynamicPropertiesSystemTestServiceLoaderOne);
        assertEquals("100", properties.getProperty(getPropName("testPropName"), null, String.class).get());
        assertEquals(new Integer(100), properties.getProperty(getPropName("testPropName"), null, Integer.class).get());
        assertEquals(new Long(100L), properties.getProperty(getPropName("testPropName"), null, Long.class).get());
        assertEquals(new Float(100.00F), properties.getProperty(getPropName("testPropName"), null, Float.class).get());
        assertEquals(Boolean.TRUE, properties.getProperty(getPropName("testPropName"), null, Boolean.class).get());
    }

    /**
     * The 'specified class implements properties' loading strategies need to be invalidated before testing.
     */
    @Test
    public void testLoadPropertiesPriorityAssertLoadSecondPolicy() {
        System.setProperty(ThreadPoolConfigDef.PLUGIN_DYNAMIC_PROPERTIES_IMPLEMENTATION, "site.halenspace.pocket.threadpool.config.DynamicPropertiesSystemTestServiceLoaderTwo");
        DynamicProperties properties = PropertiesLoadPlugin.instance().getDynamicProperties();

        assertTrue(properties instanceof DynamicPropertiesSystemTestServiceLoaderTwo);
        assertEquals("200", properties.getProperty(getPropName("testPropName"), null, String.class).get());
        assertEquals(new Integer(200), properties.getProperty(getPropName("testPropName"), null, Integer.class).get());
        assertEquals(new Long(200L), properties.getProperty(getPropName("testPropName"), null, Long.class).get());
        assertEquals(new Float(200.00F), properties.getProperty(getPropName("testPropName"), null, Float.class).get());
        assertEquals(Boolean.FALSE, properties.getProperty(getPropName("testPropName"), null, Boolean.class).get());
    }

    /**
     * The 'specified class implements properties' and 'specified path class implements properties' loading
     * strategies need to be invalidated before testing.
     */
    @Test
    public void testLoadPropertiesPriorityAssertLoadThirdPolicy() {
        System.setProperty(getPropName(ThreadPoolConfigDef.CORE_POOL_SIZE), "10");
        System.setProperty(getPropName(ThreadPoolConfigDef.MAXIMUM_POOL_SIZE), "20");
        System.setProperty(getPropName(ThreadPoolConfigDef.KEEP_ALIVE_TIME), "3600");
        DynamicProperties properties = PropertiesLoadPlugin.instance().getDynamicProperties();

        assertTrue(properties instanceof DynamicPropertiesSystem);
        assertEquals(new Integer(10), properties.getProperty(getPropName(ThreadPoolConfigDef.CORE_POOL_SIZE), null, Integer.class).get());
        assertEquals(new Integer(20), properties.getProperty(getPropName(ThreadPoolConfigDef.MAXIMUM_POOL_SIZE), null, Integer.class).get());
        assertEquals(new Long(3600), properties.getProperty(getPropName(ThreadPoolConfigDef.KEEP_ALIVE_TIME), null, Long.class).get());
    }
}
