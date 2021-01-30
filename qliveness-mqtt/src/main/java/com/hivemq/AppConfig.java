package com.hivemq;

import blxt.qjava.autovalue.inter.Component;
import blxt.qjava.autovalue.inter.Run;
import blxt.qjava.qsql.influxdb.InfluxConnectionPool;


@Component
public class AppConfig {

    @Run
    public void onConfig() throws Exception {

        InfluxConnectionPool.newInstance();

    }
}
