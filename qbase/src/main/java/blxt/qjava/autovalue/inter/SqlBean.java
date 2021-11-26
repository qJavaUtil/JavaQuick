package blxt.qjava.autovalue.inter;


import blxt.qjava.autovalue.enumbean.SymbolEnum;

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
    /** 忽略字段 */
    String[] ignores() default "";

    /** 条件符号 */
    SymbolEnum symbol() default SymbolEnum.equal;
}
