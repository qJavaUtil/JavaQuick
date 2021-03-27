package blxt.qjava.autovalue.inter;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FtpClientMark {
    String name() default "";
    String hostIp();
    int port() default 21;
    String uname();
    String upwd();
    /** 本地编码 */
    String localCharset() default "UTF8";
    /** 服务器编码 */
    String serverCharset() default "iso-8859-1";
    String path() default "/";

    /** 是否开启服务器utf8支持 */
    boolean OTP_UTF8() default true;
    /** 启用 服务器和本地编码转换*/
    boolean ChangCharset() default false;
}
