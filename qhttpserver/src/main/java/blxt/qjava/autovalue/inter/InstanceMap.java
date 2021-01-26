package blxt.qjava.autovalue.inter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 单例标记
 * @Author: Zhang.Jialei
 * @Date: 2020/9/29 14:16
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface InstanceMap {
    String value() default "";
}
