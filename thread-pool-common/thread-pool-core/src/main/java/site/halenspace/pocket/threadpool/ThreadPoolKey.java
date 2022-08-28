package site.halenspace.pocket.threadpool;

import site.halenspace.pocket.threadpool.utils.InternMap;

/**
 * @author Halen Leo · 2022/8/28
 * @blogger 后起小生
 * @github <a href="https://github.com/LeoHalen"/>
 */
public interface ThreadPoolKey {
    /**
     * Get the thread pool name (Globally unique)
     *
     * @return String
     */
    String get();

    class ThreadPoolKeyDefault implements ThreadPoolKey {
        private final String key;

        public ThreadPoolKeyDefault(String key) {
            this.key = key;
        }

        @Override
        public String get() {
            return key;
        }
    }

    class Factory {
        private static final InternMap<String, ThreadPoolKey> intern = new InternMap<>(ThreadPoolKeyDefault::new);

        private Factory() {}

        /* package-private */ static int keyCount() {
            return intern.size();
        }

        public static ThreadPoolKey asKey(String name) {
            return intern.interned(name);
        }
    }
}
