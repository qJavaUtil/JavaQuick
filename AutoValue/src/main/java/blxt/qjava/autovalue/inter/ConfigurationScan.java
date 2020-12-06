package blxt.qjava.autovalue.inter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Configuration 扫描标记
 * @Author: Zhang.Jialei
 * @Date: 2020/9/25 17:33
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigurationScan {

    String value() default "";

    boolean proxyBeanMethods() default true;
}
