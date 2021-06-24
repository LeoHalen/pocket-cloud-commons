package site.halenspace.pocketcloud.redis.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @Author Halen.Leo · 2020/6/22
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "pocket.cache-manager")
public class CacheManagerProperties {

    private List<CacheConfig> configs;

    @Setter
    @Getter
    public static class CacheConfig {

        /** redis key */
        private String key;

        /** 过期时间（seconds） */
        private long expire = 300;
    }
}
