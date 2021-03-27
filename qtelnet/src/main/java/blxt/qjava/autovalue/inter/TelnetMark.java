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
public @interface TelnetMark {

    String hostIP();
    int port() default 22;
    String uname();
    String upwd();
    String prompt() default "$";
    String termtype() default "VT100";
    // 编码转换 ORIG_CODEC -> TRANSLATE_CODEC
    String ORIG_CODEC() default "UTF-8";
    String TRANSLATE_CODEC() default "UTF-8";

}
