package blxt.qjava.autovalue.inter;

import java.lang.annotation.*;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/9/29 8:53
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    String name() default "";

    @AliasFor("value")
    String path() default "";

    @AliasFor("path")
    String value() default "";

    RequestMethod method() default RequestMethod.GET;

    String[] params() default {};

    String[] headers() default {};

    String[] consumes() default {};

    String[] produces() default {};
}
