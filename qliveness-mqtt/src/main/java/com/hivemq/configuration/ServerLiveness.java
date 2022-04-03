package com.hivemq.configuration;

import blxt.qjava.autovalue.QJavaApplication;
import blxt.qjava.autovalue.inter.Liveness;
import com.alibaba.fastjson.JSONObject;

import java.io.File;

@Liveness(tag = "mqtt", url = "http://47.242.60.114:37002/register/mqtt/insert", delay=1000 * 60 * 60 * 24)
//@Liveness(tag = "mqtt", url = "http://127.0.0.1:8081/register/mqtt/insert", delay=100, cron = "*/5 * * * * ?")
public class ServerLiveness {

    public static void main(String[] args) throws Exception {

        QJavaApplication.run(ServerLiveness.class);
        //System.out.println(fileTreeBean.toString());
    }

}
