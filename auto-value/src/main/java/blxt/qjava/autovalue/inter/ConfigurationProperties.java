package blxt.qjava.autovalue.inter;

/**
 * 预指定路径
 * 使用方式: @ConfigurationProperties(prefix = "bean.app")
 * @Author: Zhang.Jialei
 * @Date: 2020/12/7 22:32
 */

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfigurationProperties {
    String prefix() default "";

    /** 忽略无法转换的属性,这个不做了,不建议忽略无法转换的属性  */
    boolean ignoreInvalidFields() default false;
    /** 忽略没有填写key的属性 */
    boolean ignoreUnknownFields() default false;
}
