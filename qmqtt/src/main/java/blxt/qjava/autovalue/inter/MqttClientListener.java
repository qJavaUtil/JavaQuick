package blxt.qjava.autovalue.inter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ZhangJieLei
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MqttClientListener {
    /** 服务器地址 tcp://127.0.0.1:1883 */
    String url() default "${mqtt.url}";
    boolean isAuthor() default false;
    String username() default "${mqtt.username}";
    String userPwd() default "${mqtt.password}";
    String clientid() default "${mqtt.clientid}";
    /** 订阅topic */
    String[] topics() default {"${mqtt.topics}"};

    int[] qos() default {0};

}
