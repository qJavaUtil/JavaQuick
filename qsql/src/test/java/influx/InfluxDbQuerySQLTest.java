package influx;

import blxt.qjava.qsql.influxdb.InfluxDbQuerySQL;

public class InfluxDbQuerySQLTest {


    public static void main(String[] args) throws Exception {
        InfluxDbQuerySQL sql = new InfluxDbQuerySQL();
        sql.setTable("test");
        sql.setGroup("devicekey");

        System.out.println(sql.build());
    }

}