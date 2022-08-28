package site.halenspace.pocket.threadpool.config;

import site.halenspace.pocket.threadpool.config.plugins.PropertiesLoadPlugin;
import site.halenspace.pocket.threadpool.config.property.DynamicProperty;
import site.halenspace.pocket.threadpool.config.property.chain.ChainDynamicProperty;
import site.halenspace.pocket.threadpool.config.property.chain.ChainProperty;

import java.util.LinkedList;

/**
 * @author Halen Leo · 2022/8/28
 * @blogger 后起小生
 * @github <a href="https://github.com/LeoHalen"/>
 */
public class ThreadPoolPropertiesChainedProperty {
    public static ChainBuilder<String> forString() {
        return forType(String.class);
    }

    public static ChainBuilder<Integer> forInteger() {
        return forType(Integer.class);
    }

    public static ChainBuilder<Boolean> forBoolean() {
        return forType(Boolean.class);
    }

    public static ChainBuilder<Long> forLong() {
        return forType(Long.class);
    }

    public static ChainBuilder<Float> forFloat() {
        return forType(Float.class);
    }

    public static <T> ChainBuilder<T> forType(final Class<T> type) {
        return new ChainBuilder<T>() {
            @Override
            protected Class<T> getType() {
                return type;
            }
        };
    }

    public static abstract class ChainBuilder<T> {

        private final LinkedList<DynamicProperty<T>> properties = new LinkedList<>();

        protected abstract Class<T> getType();

        public ChainBuilder() {}

        public ChainBuilder<T> with(String propName, T defaultValue) {
            properties.addFirst(getDynamicProperty(propName, defaultValue, this.getType()));
            return this;
        }

        public ChainBuilder<T> with(DynamicProperty<T> property) {
            properties.addFirst(property);
            return this;
        }

        public DynamicProperty<T> build() {
            if (properties.size() < 1) throw new IllegalArgumentException();
            if (properties.size() == 1) return properties.get(0);

            // Construct the property chain link
            ChainProperty<T> current = null;
            for (DynamicProperty<T> p : properties) {
                if (current == null) {
                    current = new ChainProperty<>(p);
                }
                else {
                    current = new ChainProperty<>(p, current);
                }
            }

            return new ChainDynamicProperty<>(current);
        }

        public DynamicProperty<T> getDynamicProperty(String propName, T defValue, Class<T> type) {
            DynamicProperties dynamicProperties = PropertiesLoadPlugin.instance().getDynamicProperties();
            return dynamicProperties.getProperty(propName, defValue, type);
        }
    }
}
