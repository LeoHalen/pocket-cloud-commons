package site.halenspace.pocket.threadpool.metrics;

import com.codahale.metrics.jmx.JmxReporter;
import com.codahale.metrics.jmx.ObjectNameFactory;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.util.Hashtable;

/**
 * @author Zg.Li · 2022/9/3
 */
public class ThreadPoolObjectNameFactory implements ObjectNameFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(JmxReporter.class);
    private static final char[] QUOTABLE_CHARS = new char[] {',', '=', ':', '"'};

    @Override
    public ObjectName createName(String type, String domain, String name) {
        try {
            ObjectName objectName;
            Hashtable<String, String> properties = new Hashtable<>();

            properties.put("name", name);
            properties.put("type", type);
            objectName = new ObjectName(domain, properties);

            /*
             * The only way we can find out if we need to quote the properties is by
             * checking an ObjectName that we've constructed.
             */
            if (objectName.isDomainPattern()) {
                domain = ObjectName.quote(domain);
            }
            if (objectName.isPropertyValuePattern("name") || shouldQuote(objectName.getKeyProperty("name"))) {
                properties.put("name", ObjectName.quote(name));
            }
            if (objectName.isPropertyValuePattern("type") || shouldQuote(objectName.getKeyProperty("type"))) {
                properties.put("type", ObjectName.quote(type));
            }
            objectName = new ObjectName(domain, properties);

            return objectName;
        } catch (MalformedObjectNameException e) {
            try {
                return new ObjectName(domain, "name", ObjectName.quote(name));
            } catch (MalformedObjectNameException e1) {
                LOGGER.warn("Unable to register {} {}", type, name, e1);
                throw new RuntimeException(e1);
            }
        }
    }

    private boolean shouldQuote(final String value) {
        for (char quotableChar : QUOTABLE_CHARS) {
            if (value.indexOf(quotableChar) != -1) {
                return true;
            }
        }
        return false;
    }
}
