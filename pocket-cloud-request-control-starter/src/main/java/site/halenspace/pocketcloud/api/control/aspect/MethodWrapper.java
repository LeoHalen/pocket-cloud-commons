package site.halenspace.pocketcloud.api.control.aspect;

import java.lang.reflect.Method;

/**
 * @author Zg.Li · 2021/12/1
 */
public class MethodWrapper {

    private final Method method;
    private final boolean present;

    private MethodWrapper(Method method, boolean present) {
        this.method = method;
        this.present = present;
    }

    static MethodWrapper wrap(Method method) {
        if (method == null) {
            return none();
        }
        return new MethodWrapper(method, true);
    }

    static MethodWrapper none() {
        return new MethodWrapper(null, false);
    }

    Method getMethod() {
        return method;
    }

    boolean isPresent() {
        return present;
    }
}
