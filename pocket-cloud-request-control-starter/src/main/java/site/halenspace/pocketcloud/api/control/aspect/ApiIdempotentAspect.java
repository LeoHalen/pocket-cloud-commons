package site.halenspace.pocketcloud.api.control.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author Zg.Li · 2021/11/29
 */
@Slf4j
@Aspect
public class ApiIdempotentAspect extends AbstractAspectSupport {

    @Pointcut("@annotation(site.halenspace.pocketcloud.api.control.annotation.ApiIdempotent)")
    public void apiIdempotentAnnotationPointcut(){}

    @Around(value = "apiIdempotentAnnotationPointcut()")
    public void idempotentAroundProcess() {

    }
}
