package blxt.qjava.qmqtt;

import blxt.qjava.autovalue.reflect.PackageUtil;
import blxt.qjava.utils.check.CheckUtils;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;

public class MqttFactory {

    /**
     * 初始化mqtt对象
     * @param bean
     * @param mqttBean
     */
    public static boolean onMqttBuild(MqttClient bean, MqttBean mqttBean){
        MemoryPersistence persistence = new MemoryPersistence();
        MqttConnectionOptions connOpts = new MqttConnectionOptions();
        if(mqttBean.isAuthor){
            connOpts.setUserName(mqttBean.userName);
            connOpts.setPassword(mqttBean.password.getBytes());
        }
        try {
            bean.connect(connOpts);
        } catch (MqttException e) {
            return false;
        }
        // 添加监听
        if(PackageUtil.isInterfaces(bean.getClass(), MqttCallback.class)){
            bean.setCallback((MqttCallback)bean);
        }
        //订阅消息
        if(CheckUtils.isNotEmpty(mqttBean.topics)){
            try {
                bean.subscribe(mqttBean.topics, mqttBean.qos);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

}
