package site.halenspace.pocket.threadpool;

import site.halenspace.pocket.threadpool.utils.InternMap;

/**
 * A key to represent a {@link DynamicThreadPool} for monitoring, metrics publishing, caching and other such uses.
 * <p>
 * This interface is intended to work natively with Enums so that implementing code can be an enum that implements this interface.
 *
 * @author Halen Leo · 2021/9/5
 */
public interface DynamicThreadPoolKey extends ThreadPoolKey {

    class Factory {
        // used to intern instances so we don't keep re-creating them millions of times for the same key
        private static final InternMap<String, DynamicThreadPoolKey> intern = new InternMap<>(DynamicThreadPoolKeyDefault::new);

        private Factory() {}

        /**
         * Retrieve (or create) an interned DynamicThreadPoolKey instance for a given name.
         *
         * @param name thread pool name
         * @return DynamicThreadPoolKey instance that is interned (cached) so a given name will always retrieve the same instance.
         */
        public static DynamicThreadPoolKey asKey(String name) {
            return intern.interned(name);
        }

        /* package-private */ static int getThreadPoolCount() {
            return intern.size();
        }

        private static class DynamicThreadPoolKeyDefault extends ThreadPoolKeyDefault implements DynamicThreadPoolKey {
            public DynamicThreadPoolKeyDefault(String name) {
                super(name);
            }
        }
    }
}
