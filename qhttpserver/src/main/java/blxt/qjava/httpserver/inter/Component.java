package blxt.qjava.httpserver.inter;

import java.lang.annotation.*;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/9/29 15:54
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {
    String value() default "";
}
