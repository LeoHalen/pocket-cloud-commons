package site.halenspace.pocketcloud.threadpool.strategy.properties.archaius;

import com.netflix.config.PropertyWrapper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Halen Leo · 2021/9/5
 */
@Slf4j
public class ThreadPoolPropertiesChainedArchaiusProperty {

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

    public static class StringProperty extends ChainLink<String> {

        private final DynamicStringProperty sProp;

        public StringProperty(DynamicStringProperty sProperty) {
            super();
            sProp = sProperty;
        }

        public StringProperty(String name, DynamicStringProperty sProperty) {
            this(name, new StringProperty(sProperty));
        }

        public StringProperty(String name, StringProperty next) {
            this(new DynamicStringProperty(name, null), next);
        }

        public StringProperty(DynamicStringProperty sProperty, DynamicStringProperty next) {
            this(sProperty, new StringProperty(next));
        }

        public StringProperty(DynamicStringProperty sProperty, StringProperty next) {
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
        protected String getValue() {
            return sProp.get();
        }

        @Override
        public String getName() {
            return sProp.getName();
        }
    }

    public static class IntegerProperty extends ChainLink<Integer> {

        private final DynamicIntegerProperty sProp;

        public IntegerProperty(DynamicIntegerProperty sProperty) {
            super();
            sProp = sProperty;
        }

        public IntegerProperty(String name, DynamicIntegerProperty sProperty) {
            this(name, new IntegerProperty(sProperty));
        }

        public IntegerProperty(String name, IntegerProperty next) {
            this(new DynamicIntegerProperty(name, null), next);
        }

        public IntegerProperty(DynamicIntegerProperty sProperty, DynamicIntegerProperty next) {
            this(sProperty, new IntegerProperty(next));
        }

        public IntegerProperty(DynamicIntegerProperty sProperty, IntegerProperty next) {
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
        public Integer getValue() {
            return sProp.get();
        }

        @Override
        public String getName() {
            return sProp.getName();
        }
    }

    public static class BooleanProperty extends ChainLink<Boolean> {

        private final DynamicBooleanProperty sProp;

        public BooleanProperty(DynamicBooleanProperty sProperty) {
            super();
            sProp = sProperty;
        }

        public BooleanProperty(String name, DynamicBooleanProperty sProperty) {
            this(name, new BooleanProperty(sProperty));
        }

        public BooleanProperty(String name, BooleanProperty next) {
            this(new DynamicBooleanProperty(name, null), next);
        }

        public BooleanProperty(DynamicBooleanProperty sProperty, DynamicBooleanProperty next) {
            this(sProperty, new BooleanProperty(next));
        }

        public BooleanProperty(DynamicBooleanProperty sProperty, BooleanProperty next) {
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
            return (sProp.getValue() != null);
        }

        @Override
        public Boolean getValue() {
            return sProp.get();
        }

        @Override
        public String getName() {
            return sProp.getName();
        }
    }

    public static class DynamicBooleanProperty extends PropertyWrapper<Boolean> {
        public DynamicBooleanProperty(String propName, Boolean defaultValue) {
            super(propName, defaultValue);
        }

        /**
         * Get the current value from the underlying DynamicProperty
         */
        public Boolean get() {
            return prop.getBoolean(defaultValue);
        }

        @Override
        public Boolean getValue() {
            return get();
        }
    }

    public static class DynamicIntegerProperty extends PropertyWrapper<Integer> {
        public DynamicIntegerProperty(String propName, Integer defaultValue) {
            super(propName, defaultValue);
        }

        /**
         * Get the current value from the underlying DynamicProperty
         */
        public Integer get() {
            return prop.getInteger(defaultValue);
        }

        @Override
        public Integer getValue() {
            return get();
        }
    }

    public static class DynamicLongProperty extends PropertyWrapper<Long> {
        public DynamicLongProperty(String propName, Long defaultValue) {
            super(propName, defaultValue);
        }

        /**
         * Get the current value from the underlying DynamicProperty
         */
        public Long get() {
            return prop.getLong(defaultValue);
        }

        @Override
        public Long getValue() {
            return get();
        }
    }

    public static class DynamicFloatProperty extends PropertyWrapper<Float> {
        public DynamicFloatProperty(String propName, Float defaultValue) {
            super(propName, defaultValue);
        }

        /**
         * Get the current value from the underlying DynamicProperty
         */
        public Float get() {
            return prop.getFloat(defaultValue);
        }

        @Override
        public Float getValue() {
            return get();
        }
    }

    public static class DynamicStringProperty extends PropertyWrapper<String> {
        public DynamicStringProperty(String propName, String defaultValue) {
            super(propName, defaultValue);
        }

        /**
         * Get the current value from the underlying DynamicProperty
         */
        public String get() {
            return prop.getString(defaultValue);
        }

        @Override
        public String getValue() {
            return get();
        }
    }
}
