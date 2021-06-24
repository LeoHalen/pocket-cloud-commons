package site.halenspace.pocketcloud.redis.serializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author Halen.Leo · 2020/6/23
 */
public class RedisObjectSerializer extends Jackson2JsonRedisSerializer<Object> {

    protected final static String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public RedisObjectSerializer() {
        super(Object.class);
        ObjectMapper om = new ObjectMapper().setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
                // 过期方法
//                .enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL)
                .activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true)
                .setDateFormat(new SimpleDateFormat(NORM_DATETIME_PATTERN));
        SimpleModule simpleModule = new SimpleModule()
                // 枚举转换器
//                .addDeserializer(Enum.class, EnumDeserializer.INSTANCE)
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(NORM_DATETIME_PATTERN)))
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(NORM_DATETIME_PATTERN)));
        om.registerModule(simpleModule);
        this.setObjectMapper(om);

    }
}
