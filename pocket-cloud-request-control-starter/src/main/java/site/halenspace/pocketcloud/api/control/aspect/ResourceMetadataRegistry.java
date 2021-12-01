package site.halenspace.pocketcloud.api.control.aspect;

import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zg.Li · 2021/12/1
 */
public class ResourceMetadataRegistry {

    private static final Map<String, MethodWrapper> FALLBACK_MAP = new ConcurrentHashMap<>();

    static MethodWrapper lookupFallback(Class<?> clazz, String name) {
        return FALLBACK_MAP.get(getKey(clazz, name));
    }

    static void updateFallbackFor(Class<?> clazz, String name, Method method) {
        if (clazz == null || StrUtil.isBlank(name)) {
            throw new IllegalArgumentException("Bad argument");
        }
        FALLBACK_MAP.put(getKey(clazz, name), MethodWrapper.wrap(method));
    }

    private static String getKey(Class<?> clazz, String name) {
        return String.format("%s:%s", clazz.getCanonicalName(), name);
    }

    /**
     * Only for internal test.
     */
    static void clearFallbackMap() {
        FALLBACK_MAP.clear();
    }

}
