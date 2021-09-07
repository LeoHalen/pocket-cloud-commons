package site.halenspace.pocketcloud.threadpool.strategy.properties.archaius;

import lombok.extern.slf4j.Slf4j;
import site.halenspace.pocketcloud.threadpool.strategy.ThreadPoolPlugins;
import site.halenspace.pocketcloud.threadpool.strategy.properties.ThreadPoolDynamicProperties;
import site.halenspace.pocketcloud.threadpool.strategy.properties.property.ThreadPoolDynamicProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Halen Leo · 2021/9/5
 */
@Slf4j
public class ThreadPoolPropertiesChainedProperty {

    public static abstract class ChainLink<T> {

        private final AtomicReference<ChainLink<T>> aReference;
        private final ChainLink<T> next;
        private final List<Runnable> callbacks;

        public abstract String getName();

        protected abstract T getValue();

        public abstract boolean isValueAcceptable();

        /**
         * No arg constructor - used for end node
         */
        public ChainLink() {
            next = null;
            aReference = new AtomicReference<>(this);
            callbacks = new ArrayList<>();
        }

        /**
         * @param nextProperty next property in the chain
         */
        public ChainLink(ChainLink<T> nextProperty) {
            next = nextProperty;
            aReference = new AtomicReference<>(next);
            callbacks = new ArrayList<>();
        }

        protected void checkAndFlip() {
            // in case this is the end node
            if (next == null) {
                aReference.set(this);
                return;
            }

            if (this.isValueAcceptable()) {
                log.debug("Flipping property: {} to use its current value: {}", getName(), getValue());
                aReference.set(this);
            } else {
                log.debug("Flipping property: {} to use NEXT property: {}", getName(), next);
                aReference.set(next);
            }

            for (Runnable r : callbacks) {
                r.run();
            }
        }

        public T get() {
            if (aReference.get() == this) {
                return this.getValue();
            } else {
                return aReference.get().get();
            }
        }

        /**
         * @param r callback to execut
         */
        public void addCallback(Runnable r) {
            callbacks.add(r);
        }

        public String toString() {
            return getName() + " = " + get();
        }
    }

    public static abstract class ChainBuilder<T> {

        private final List<ThreadPoolDynamicProperty<T>> properties = new ArrayList<>();

        private ChainBuilder() {
            super();
        }

        public ChainBuilder<T> add(ThreadPoolDynamicProperty<T> property) {
            properties.add(property);
            return this;
        }

        public ChainBuilder<T> add(String name, T defaultValue) {
            properties.add(getDynamicProperty(name, defaultValue, getType()));
            return this;
        }

        public ThreadPoolDynamicProperty<T> build() {
            if (properties.size() < 1) throw new IllegalArgumentException();
            if (properties.size() == 1) return properties.get(0);
            List<ThreadPoolDynamicProperty<T>> reversed = new ArrayList<>(properties);
            Collections.reverse(reversed);
            ChainProperty<T> current = null;
            for (ThreadPoolDynamicProperty<T> p : reversed) {
                if (current == null) {
                    current = new ChainProperty<>(p);
                }
                else {
                    current = new ChainProperty<>(p, current);
                }
            }

            return new ChainThreadPoolProperty<>(current);
        }

        protected abstract Class<T> getType();

    }

    private static <T> ChainBuilder<T> forType(final Class<T> type) {
        return new ChainBuilder<T>() {
            @Override
            protected Class<T> getType() {
                return type;
            }
        };
    }

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

    private static class ChainThreadPoolProperty<T> implements ThreadPoolDynamicProperty<T> {
        private final ChainProperty<T> property;

        public ChainThreadPoolProperty(ChainProperty<T> property) {
            super();
            this.property = property;
        }

        @Override
        public String getName() {
            return property.getName();
        }

        @Override
        public T get() {
            return property.get();
        }

        @Override
        public void addCallback(Runnable callback) {
            property.addCallback(callback);
        }

    }

    private static class ChainProperty<T> extends ChainLink<T> {

        private final ThreadPoolDynamicProperty<T> sProp;

        public ChainProperty(ThreadPoolDynamicProperty<T> sProperty) {
            super();
            sProp = sProperty;
        }


        public ChainProperty(ThreadPoolDynamicProperty<T> sProperty, ChainProperty<T> next) {
            super(next); // setup next pointer

            sProp = sProperty;
            sProp.addCallback(() -> {
                log.debug("Property changed: '{} = {}'", getName(), getValue());
                checkAndFlip();
            });
            checkAndFlip();
        }

        @Override
        public boolean isValueAcceptable() {
            return (sProp.get() != null);
        }

        @Override
        protected T getValue() {
            return sProp.get();
        }

        @Override
        public String getName() {
            return sProp.getName();
        }

    }

    private static <T> ThreadPoolDynamicProperty<T> getDynamicProperty(String propName, T defaultValue, Class<T> type) {
        ThreadPoolDynamicProperties properties = ThreadPoolPlugins.getInstance().getDynamicProperties();
        return ThreadPoolDynamicProperties.Util.getProperty(properties, propName, defaultValue, type);
    }
}
