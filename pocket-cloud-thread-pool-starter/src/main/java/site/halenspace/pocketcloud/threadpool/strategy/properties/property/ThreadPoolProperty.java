package site.halenspace.pocketcloud.threadpool.strategy.properties.property;

import site.halenspace.pocketcloud.threadpool.strategy.properties.archaius.ThreadPoolPropertiesChainedArchaiusProperty.*;

/**
 * @author Halen Leo · 2021/9/5
 */
public interface ThreadPoolProperty<T> {

    T get();

    class Factory {

        public static <T> ThreadPoolProperty<T> asProperty(final T value) {
            return () -> value;
        }

        public static ThreadPoolProperty<Integer> asProperty(final DynamicIntegerProperty value) {
            return value::get;
        }

        public static ThreadPoolProperty<Long> asProperty(final DynamicLongProperty value) {
            return value::get;
        }

        public static ThreadPoolProperty<String> asProperty(final DynamicStringProperty value) {
            return value::get;
        }

        public static ThreadPoolProperty<Boolean> asProperty(final DynamicBooleanProperty value) {
            return value::get;
        }

        public static ThreadPoolProperty<Float> asProperty(final DynamicFloatProperty value) {
            return value::get;
        }

        public static <T> ThreadPoolProperty<T> asProperty(final ThreadPoolProperty<T> value, final T defaultValue) {
            return () -> {
                T v = value.get();
                if (v == null) {
                    return defaultValue;
                } else {
                    return v;
                }
            };
        }

        /**
         * When retrieved this will iterate over the contained {@link ThreadPoolProperty} instances until a non-null value is found and return that.
         *
         * @param values properties to iterate over
         * @return first non-null value or null if none found
         */
        public static <T> ThreadPoolProperty<T> asProperty(final ThreadPoolProperty<T>... values) {
            return () -> {
                for (ThreadPoolProperty<T> v : values) {
                    // return the first one that doesn't return null
                    if (v.get() != null) {
                        return v.get();
                    }
                }
                return null;
            };
        }

        public static <T> ThreadPoolProperty<T> asProperty(final ChainLink<T> chainedProperty) {
            return chainedProperty::get;
        }

        public static <T> ThreadPoolProperty<T> nullProperty() {
            return () -> null;
        }
    }
}
