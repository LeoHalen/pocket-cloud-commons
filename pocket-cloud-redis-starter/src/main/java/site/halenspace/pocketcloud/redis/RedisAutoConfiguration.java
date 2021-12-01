package site.halenspace.pocketcloud.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import site.halenspace.pocketcloud.redis.properties.CacheManagerProperties;
import site.halenspace.pocketcloud.redis.serializer.RedisObjectSerializer;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author Halen Leo · 2020/6/22
 */
@ConditionalOnProperty(name = "pocket.cache-manager.enabled", matchIfMissing = true)
@EnableConfigurationProperties(CacheManagerProperties.class)
@EnableCaching
@Slf4j
public class RedisAutoConfiguration {
    @Autowired
    private CacheManagerProperties cacheManagerProperties;

    @PostConstruct
    public void init() {
        log.info("Common 'Cache-Manager': enabled");
    }

    @Bean
    public RedisSerializer<String> redisKeySerializer() {
        return RedisSerializer.string();
    }

    @Bean
    public RedisSerializer<Object> redisValueSerializer() {
//        return RedisSerializer.java();
        // 替换使用Jackson2JsonRedisSerializer序列化器
//        return RedisSerializer.json();
        // 自定义Jackson2序列化器
        return new RedisObjectSerializer();
    }

    /**
     * redis template配置
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory factory,
                                                       RedisSerializer<String> redisKeySerializer,
                                                       RedisSerializer<Object> redisValueSerializer) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(redisKeySerializer);
        redisTemplate.setValueSerializer(redisValueSerializer);
        redisTemplate.setHashKeySerializer(redisKeySerializer);
        redisTemplate.setHashValueSerializer(redisValueSerializer);

        return redisTemplate;
    }

    /**
     * 自定义key生成规则
     * rule:
     *  className + ":" + methodName + ":" + paramArg1 + ":" + ... + ":" + paramArgn
     */
    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringJoiner keyJoiner = new StringJoiner(":");
            keyJoiner.add(target.getClass().getName());
            keyJoiner.add(method.getName());
            for (Object param : params) {
                keyJoiner.add(param.toString());
            }

            return keyJoiner.toString();
        };
    }

    /**
     * spring 缓存管理器（这里使用redis缓存管理器）
     */
    @Bean
    public CacheManager cacheManager(LettuceConnectionFactory factory,
                                     RedisSerializer<String> redisKeySerializer,
                                     RedisSerializer<Object> redisValueSerializer) {
        RedisCacheConfiguration defaultCacheConfiguration =
                getDefaultCacheConfiguration(redisKeySerializer, redisValueSerializer).entryTtl(Duration.ofSeconds(3600));
        Map<String, RedisCacheConfiguration> customCacheConfigurations =
                getCustomCacheConfigurations(redisKeySerializer, redisValueSerializer);

        return RedisCacheManager.builder(factory)
                .cacheDefaults(defaultCacheConfiguration)
                .withInitialCacheConfigurations(customCacheConfigurations)
                .build();
    }

    /**
     * 获取默认的redisCache配置
     */
    private RedisCacheConfiguration getDefaultCacheConfiguration(RedisSerializer<String> redisKeySerializer,
                                                                 RedisSerializer<Object> redisValueSerializer) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .computePrefixWith(cacheKeyPrefix -> "cache".concat(":").concat(cacheKeyPrefix).concat(":"))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisKeySerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisValueSerializer));
    }

    /**
     * 获取自定义的redisCache配置
     */
    private Map<String, RedisCacheConfiguration> getCustomCacheConfigurations(RedisSerializer<String> redisKeySerializer,
                                                                              RedisSerializer<Object> redisValueSerializer) {
        //自定义的缓存过期时间配置
        int configSize = cacheManagerProperties.getConfigs() == null ? 0 : cacheManagerProperties.getConfigs().size();
        Map<String, RedisCacheConfiguration> redisCacheConfigurations = new HashMap<>(configSize);
        if (configSize > 0) {
            cacheManagerProperties.getConfigs().forEach(cacheConfig -> {
                RedisCacheConfiguration conf = getDefaultCacheConfiguration(redisKeySerializer, redisValueSerializer)
                        .entryTtl(Duration.ofSeconds(cacheConfig.getExpire()));
                redisCacheConfigurations.put(cacheConfig.getKey(), conf);
            });
        }

        return redisCacheConfigurations;
    }
}
