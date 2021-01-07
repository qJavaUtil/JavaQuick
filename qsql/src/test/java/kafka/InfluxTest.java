package kafka;

import blxt.qjava.qsql.influxdb.InfluxConnection;
import blxt.qjava.qsql.influxdb.InfluxConnectionManager;
import blxt.qjava.qsql.influxdb.InfluxDbQuerySQL;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/8/10 15:21
 */
public class InfluxTest {
    InfluxConnectionManager connectionManager;
    InfluxConnection connectionDef;

    public void Get(String userName, String password, String url, String database,
                       String retentionPolicy) {
        connectionManager =
                InfluxConnectionManager.newInstance("root", "influx",
                        "http://39.101.205.151:8086" ,
                        "hivemq", "default");
        // 默认创建一个设备-用户表,如果连接不上,那就说明数据库没有连上
        connectionDef = connectionManager.add("system.users.u123");
        // connectionDef = connectionManager.add("system.users.u123");

        InfluxDbQuerySQL sql = new InfluxDbQuerySQL();
      //  sql.setGroup();
    }

}
