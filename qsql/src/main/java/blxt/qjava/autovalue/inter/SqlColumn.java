package blxt.qjava.autovalue.inter;

import blxt.qjava.autovalue.bean.JoinEnum;

import java.lang.annotation.*;

/**
 * SqlColumn 字段定义
 * @author ZhangJieLei
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SqlColumn {
    /** 字段值 */
    String value() default "";
    /** 类型 */
    String method() default "";
    /** 是否可以是null */
    boolean isNull() default true;
    /** 默认值 */
    String defaultValue() default "";
    /** 字段描述 */
    String comment() default "";

    /** 函数 */
    String function() default "";

    /** 模糊查询 */
    boolean selectLike() default false;

    /** select 中可见 */
    boolean selectEnable() default true;
    /** 自增序列*/
    boolean sequence() default false;
    /** 自动更新触发 */
    boolean autoupdate() default false;

    /** 连表, A表名 */
    String joinTableA() default "";
    /** 连表, B表名 */
    String joinTableB() default "";
    /** 连表, B表字段 */
    String joinColumB() default "";
    /** 连表, A表关联键 */
    String joinKeyA() default "";
    /** 连表, B表关联键 */
    String joinKeyB() default "";
    /** 关联查询
     * 方式: JOIN, ()LEFT OUTER JOIN,  RIGHT OUT JOIN,  (外连接)FULL OUTER JOIN
     * */
    JoinEnum join() default JoinEnum.PG_JOIN;

}
