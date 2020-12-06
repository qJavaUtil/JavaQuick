package blxt.qjava.httpserver.inter;

import java.lang.annotation.*;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/9/29 15:55
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseBody {
}

