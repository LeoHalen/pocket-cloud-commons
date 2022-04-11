package site.halenspace.pocketcloud.redis.tool;

import site.halenspace.pocketcloud.redis.exception.ClassConvertException;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Halen Leo · 2022/4/11
 */
public class ConversionTool {

    /**
     * 对象类型转换
     * @param obj 待转换类实例引用
     * @param clazz 目标类型
     * @param <T> 泛型
     * @return 目标类型实例引用
     */
    @SuppressWarnings("unchecked")
    public static <T> T to(Object obj, Class<T> clazz) {
        if (obj == null) {
            return null;
        }
        if (clazz == null || !clazz.isInstance(obj)) {
            throw new ClassConvertException("the target type cannot be converted");
        }
        return (T) obj;
    }

    @SuppressWarnings("unchecked")
    public static <T> Set<T> toSet(Object obj, Class<T> clazz) {
        if (obj == null) {
            return null;
        }
        if (clazz == null) {
            throw new ClassConvertException("the target type cannot be converted");
        }
        return (Set<T>) obj;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(Object obj, Class<T> clazz) {
        if (obj == null) {
            return null;
        }
        if (clazz == null) {
            throw new ClassConvertException("the target type cannot be converted");
        }
        return (List<T>) obj;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> to(List<Object> collection, Class<T> targetType) {
        if (collection == null) {
            return null;
        }
        if (targetType == null) {
            throw new ClassConvertException("the target type is required");
        }

        return (List<T>) collection;
    }

    @SuppressWarnings("unchecked")
    public static <HV> Map<String, HV> toMap(Map<String, Object> map, Class<HV> valueType) {
        if (map == null) {
            return null;
        }
        if (valueType == null) {
            throw new ClassConvertException("the value type is required");
        }

        return (Map<String, HV>) map;
    }

    @SuppressWarnings("unchecked")
    public static <HK, HV> Map<HK, HV> toMap(Map<Object, Object> map, Class<HK> keyType, Class<HV> valueType) {
        if (map == null) {
            return null;
        }
        if (keyType == null) {
            throw new ClassConvertException("the key type is required");
        }
        if (valueType == null) {
            throw new ClassConvertException("the value type is required");
        }

        return (Map<HK, HV>) map;
    }
}
