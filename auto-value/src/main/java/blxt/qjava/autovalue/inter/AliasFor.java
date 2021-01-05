package blxt.qjava.autovalue.inter;

import java.lang.annotation.*;

/**
 * 入参/方法 别名
 * @Author: Zhang.Jialei
 * @Date: 2020/12/8 14:26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.PARAMETER})
@Documented
public @interface AliasFor {

    String value() default "";

    String attribute() default "";

    Class<? extends Annotation> annotation() default Annotation.class;
}

