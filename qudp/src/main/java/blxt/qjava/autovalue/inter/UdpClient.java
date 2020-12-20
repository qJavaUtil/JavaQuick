package blxt.qjava.autovalue.inter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 创建一个UdpClient,向指定主机发起连接
 * @Author: Zhang.Jialei
 * @Date: 2020/12/17 21:00
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UdpClient {
    /** 发送目标IP地址 */
    String hostIp();
    /** 监听端口  */
    int port();
}
