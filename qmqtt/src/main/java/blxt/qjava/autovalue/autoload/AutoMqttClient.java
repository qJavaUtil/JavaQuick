package blxt.qjava.autovalue.autoload;

import blxt.qjava.autovalue.inter.MqttClientListener;
import blxt.qjava.autovalue.inter.autoload.AutoLoadFactory;
import blxt.qjava.autovalue.util.ObjectPool;
import blxt.qjava.qmqtt.MqttBean;
import blxt.qjava.qmqtt.MqttFactory;
import blxt.qjava.utils.PropertiesTools;
import org.eclipse.paho.mqttv5.client.MqttClient;

/**
 * mqtt客户端订阅
 */
@AutoLoadFactory(name="MqttClientListener", annotation = MqttClientListener.class, priority = 20)
public class AutoMqttClient extends AutoLoadBase{

    @Override
    public <T> T inject(Class<?> object) throws Exception {

        MqttClientListener anno = object.getAnnotation(MqttClientListener.class);
        if(anno == null){
            return null;
        }

        if (!MqttClient.class.isAssignableFrom(object)) {
            throw new IllegalArgumentException(
                    object.getName() + "MqttClientListener需要集成org.eclipse.paho.mqttv5.client.MqttClient类。");
        }

        // 构造
        MqttBean mqttBean = new MqttBean(anno.url().trim(), anno.clientid(),
                anno.username(), anno.userPwd(), anno.topics(), anno.qos(), anno.isAuthor());
        if(PropertiesTools.isKey1(mqttBean.url)){
            String key = checKeyNull(mqttBean.url);
            mqttBean.url = (String) AutoValue.getPropertiesValue(key, String.class);
        }
        if(PropertiesTools.isKey1(mqttBean.clientid)){
            String key = checKeyNull(mqttBean.clientid);
            mqttBean.clientid = (String) AutoValue.getPropertiesValue(key, String.class);
        }
        if(PropertiesTools.isKey1(mqttBean.userName)){
            String key = checKeyNull(mqttBean.userName);
            mqttBean.userName = (String) AutoValue.getPropertiesValue(key, String.class);
        }
        if(PropertiesTools.isKey1(mqttBean.password)){
            String key = checKeyNull(mqttBean.password);
            mqttBean.password = (String) AutoValue.getPropertiesValue(key, String.class);
        }
        int i = 0;
        for(String s : mqttBean.topics){
            if(PropertiesTools.isKey1(s)){
                String key = checKeyNull(s);
                mqttBean.topics[i++] = (String) AutoValue.getPropertiesValue(key, String.class);
            }
        }

        // 链接和客户端id
        String[] params = new String[]{mqttBean.url, mqttBean.clientid};
        // 自动实现
        T bean = ObjectPool.putObjectWithParams(object, params);
        if(!MqttFactory.onMqttBuild((MqttClient)bean, mqttBean)){
            ObjectPool.remove(object);
            throw new Exception("mqtt链接失败:");
        }

        return bean;
    }

    /**
     * 属性检查
     * @param key
     * @throws Exception
     */
    private String checKeyNull(String key) throws Exception {
        String value = PropertiesTools.getKey1(key);
        if(AutoValue.isNull(value)){
            throw new Exception("属性值不能为空: " + key);
        }
        return value;
    }

}
