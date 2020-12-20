package blxt.qjava.autovalue.inter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Udp端口监听注解 ,创建一个udpServer,监听固定端口
 * @Author: Zhang.Jialei
 * @Date: 2020/12/17 21:00
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UdpListener {
    /** 监听端口  */
    int port();
    /** 默认读包大小 */
    int packageSize() default 1024;

}
