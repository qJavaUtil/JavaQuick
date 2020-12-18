package blxt.qjava.autovalue.inter.autoload;

import java.lang.annotation.*;

/**
 * 自动load工厂标记
 * @Author: Zhang.Jialei
 * @Date: 2020/12/18 16:07
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoLoadFactory {
    /** 扫描接口名  **/
    Class<? extends Annotation> annotation();
    int priority() default 10;
}
