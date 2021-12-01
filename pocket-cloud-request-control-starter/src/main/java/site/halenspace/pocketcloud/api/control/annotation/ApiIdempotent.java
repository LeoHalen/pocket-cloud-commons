package site.halenspace.pocketcloud.api.control.annotation;

import java.lang.annotation.*;

/**
 * @author Zg.Li · 2021/11/29
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiIdempotent {
}
