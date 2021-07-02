package blxt.qjava.autovalue.inter;

import java.lang.annotation.*;

/**
 * @author OpenJialei
 * @date 2021年07月01日 15:06
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SelectName {
    /** sql 查询时, 列名称 */
    String value();
}
