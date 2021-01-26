package blxt.qjava.autovalue.inter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启httpserver
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnHttpServer {
    /** 服务端口 */
    int port() default 8080;
    /** 默认backlog */
    int backlog() default 0;
    /** 默认根路径 */
    String path() default "/";
}
