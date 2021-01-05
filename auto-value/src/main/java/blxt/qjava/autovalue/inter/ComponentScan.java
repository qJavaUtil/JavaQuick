package blxt.qjava.autovalue.inter;

import java.lang.annotation.*;

/**
 * 类自动装载标记
 * @Author: Zhang.Jialei
 * @Date: 2020/12/4 12:38
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ComponentScan {
    String value() default "";
}
