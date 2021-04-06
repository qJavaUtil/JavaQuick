package blxt.qjava.autovalue.inter;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * telnet
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SshMark {

    String tag() default "default";
    String hostIP();
    int port() default 22;
    String uname();
    String upwd();
    String CODEC() default "UTF-8";

}
