package blxt.qjava.autovalue.inter;

import java.lang.annotation.*;

/**
 * 类变量自动装载标记
 * @Author: Zhang.Jialei
 * @Date: 2020/12/4 12:37
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    boolean required() default true;
}
