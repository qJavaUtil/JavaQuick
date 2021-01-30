package com.hivemq;

import blxt.qjava.autovalue.QJavaApplication;
import blxt.qjava.autovalue.inter.ComponentScan;
import blxt.qjava.autovalue.inter.ConfigurationScan;
import blxt.qjava.autovalue.inter.EnHttpServer;
import blxt.qjava.autovalue.inter.Liveness;

@EnHttpServer()
@ComponentScan()
@ConfigurationScan()
@Liveness(tag = "mqtt", url = "http://qjava.register.zhangjialei:60001/qliveness/register")
public class UserClientInfoListenerApp {

    public static void main(String[] args) throws Exception {

        QJavaApplication.run(UserClientInfoListenerApp.class);

//        Register register = new Register();
//        register.setArl("http://192.168.1.113:8080/test/register");
//        register.setAppTage("mqtt");
//        register.register();
    }


}
