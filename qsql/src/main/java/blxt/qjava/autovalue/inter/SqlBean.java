package blxt.qjava.autovalue.inter;

import java.lang.annotation.*;

/**
 * sql对象定义
 * @author ZhangJieLei
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SqlBean {
    /** 表名 */
    String value() default "";
    /** 主键字段 */
    String keyId();
}
