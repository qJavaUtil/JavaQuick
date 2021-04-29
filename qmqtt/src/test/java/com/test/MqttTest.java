package com.test;

import blxt.qjava.autovalue.autoload.AutoMqttClient;
import blxt.qjava.autovalue.inter.MqttClientListener;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;


@MqttClientListener(url="tcp://192.168.0.103:1883", isAuthor=true, username="admin-user", userPwd="666666",
        topics={"system.broadcast"}, clientid="system.admin.nio")
public class MqttTest extends MqttClient implements MqttCallback {

    public static void main(String[] args) throws Exception {
        AutoMqttClient autoMqttClient = new AutoMqttClient();
        autoMqttClient.inject(MqttTest.class);
    }

    public MqttTest(String serverURI, String clientId) throws MqttException {
        super(serverURI, clientId);

    }

    @Override
    public void disconnected(MqttDisconnectResponse mqttDisconnectResponse) {
       // 链接断开
    }

    @Override
    public void mqttErrorOccurred(MqttException e) {
      // 消息错误
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        System.out.println(String.format("收到数据:%s, %s", s , mqttMessage.toDebugString()));
    }

    @Override
    public void deliveryComplete(IMqttToken iMqttToken) {

    }

    @Override
    public void connectComplete(boolean b, String s) {

    }

    @Override
    public void authPacketArrived(int i, MqttProperties mqttProperties) {

    }
}
