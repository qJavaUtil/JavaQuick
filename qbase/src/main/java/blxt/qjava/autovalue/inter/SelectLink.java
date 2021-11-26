package blxt.qjava.autovalue.inter;

/**
 * @author OpenJialei
 * @date 2021年07月20日 11:48
 */


import blxt.qjava.autovalue.enumbean.JoinEnum;

import java.lang.annotation.*;

/**
 * SelectLink 字段 连表
 * @author ZhangJieLei
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SelectLink {

    /** 连表, A表名 */
    String joinTableA() default "";
    /** 连表, B表名 */
    String joinTableB() default "";
    /** 连表, B表名,  新名字 */
    String joinTableBrname() default "";
    /** 连表, B表字段 */
    String joinColumB() default "";
    /** 连表, B表 函数 */
    String functionB() default "";
    /** 连表, A表关联键 */
    String joinKeyA() default "";
    /** 连表, B表关联键 */
    String joinKeyB() default "";
    /** 关联查询
     * 方式: JOIN, ()LEFT OUTER JOIN,  RIGHT OUT JOIN,  (外连接)FULL OUTER JOIN
     * */
    JoinEnum join() default JoinEnum.PG_JOIN;

}
