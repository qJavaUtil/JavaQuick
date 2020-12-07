package blxt.qjava.autovalue.inter;

import java.lang.annotation.*;

/**
 * Properties 文件源注解
 * 指定properties文件路径和编码
 * 使用: @PropertySource(encoding = "utf-8", value="./resources/application2.properties")
 * @Author: Zhang.Jialei
 * @Date: 2020/12/7 21:25
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PropertySource {
    String name() default "";

    String value() default "";

    boolean ignoreResourceNotFound() default false;

    String encoding() default "utf-8";

}
