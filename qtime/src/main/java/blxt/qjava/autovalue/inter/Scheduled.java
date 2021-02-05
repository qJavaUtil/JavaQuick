package blxt.qjava.autovalue.inter;

import java.lang.annotation.*;

/**
 * 定时器注解
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scheduled {

    String CRON_DISABLED = "-";

    String cron() default "";

    String zone() default "";

    String group() default "";

    String trigger_group() default "";

    long fixedDelay() default -1L;

    /**  固定延时 */
    String fixedDelayString() default "";

    long fixedRate() default -1L;
    /** 固定频率 */
    String fixedRateString() default "";

    long initialDelay() default -1L;

    String initialDelayString() default "";
}
