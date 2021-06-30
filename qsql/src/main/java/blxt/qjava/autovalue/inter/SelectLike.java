package blxt.qjava.autovalue.inter;

import java.lang.annotation.*;

/**
 * 模糊查询
 * @author OpenJialei
 * @date 2021年06月30日 17:24
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SelectLike {
    String value() default "";
}
