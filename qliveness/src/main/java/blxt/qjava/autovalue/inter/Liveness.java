package blxt.qjava.autovalue.inter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Liveness {
    String url();
    String[] tag();
    String cron() default "59 59 0 15 * ?";
    int delay() default 60 * 1000;
}
