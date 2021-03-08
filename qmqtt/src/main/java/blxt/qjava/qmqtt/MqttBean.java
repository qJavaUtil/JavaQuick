package blxt.qjava.qmqtt;

import lombok.Data;

/**
 * mqtt链接信息
 */
@Data
public class MqttBean {
    public String url;
    public String clientid;
    public String userName;
    public String password;

    public String[] topics;
    /** 消息质量 0-最多一次 1-至少一次 2-刚好一次 */
    public int[] qos = {0};
    /** 是否进行登录验证 */
    public boolean isAuthor;

    public MqttBean(){}

    public MqttBean(String url, String clientid, String userName, String password, String[] topics, int[] qos, boolean isAuthor) {
        this.url = url;
        this.clientid = clientid;
        this.userName = userName;
        this.password = password;
        this.topics = topics;
        this.qos = qos;
        this.isAuthor = isAuthor;
    }
}
