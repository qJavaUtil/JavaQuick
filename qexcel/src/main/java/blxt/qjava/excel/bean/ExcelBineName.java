package blxt.qjava.excel.bean;


import java.lang.annotation.*;

/**
 * Excel 表头名字绑定
 * @author OpenJialei
 * @date 2021年08月21日 17:33
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface ExcelBineName {

    /** 列名 */
    String column();
    /** 单元格格式 */
    String style() default "@";
    /** 文字格式 */
    String format() default "null";
}
