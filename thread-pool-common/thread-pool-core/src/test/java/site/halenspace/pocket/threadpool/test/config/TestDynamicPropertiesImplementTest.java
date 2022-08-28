package site.halenspace.pocket.threadpool.test.config;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Zg.Li · 2022/8/11
 */
public class TestDynamicPropertiesImplementTest {

    private final TestDynamicPropertiesImplement properties = TestDynamicPropertiesImplement.instance();

    private static String getPropName(String name) {
        return "dynamic.thread-pool.test-pool." + name;
    }

    @Test
    public void testSystemPropertyAcquisition() {

        assertEquals("100", properties.getProperty(getPropName("testPropName"), null, String.class).get());
        assertEquals(new Integer(100), properties.getProperty(getPropName("testPropName"), null, Integer.class).get());
        assertEquals(new Long(100L), properties.getProperty(getPropName("testPropName"), null, Long.class).get());
        assertEquals(new Float(100.00F), properties.getProperty(getPropName("testPropName"), null, Float.class).get());
        assertEquals(Boolean.TRUE, properties.getProperty(getPropName("testPropName"), null, Boolean.class).get());
    }

}
