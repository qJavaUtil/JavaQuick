import blxt.qjava.qsql.influxdb.InfluxBean;
import blxt.qjava.qsql.influxdb.InfluxConnection;
import blxt.qjava.qsql.influxdb.InfluxConnectionPool;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class IndexTest {


    public static void main(String[] args) throws Exception {

        InfluxBean influxBean = new InfluxBean("root", "zxcvbnm", "http://192.168.3.20:8086", "", "default", false);
        InfluxConnectionPool influxConnectionPool = InfluxConnectionPool.newInstance(influxBean);

        InfluxConnection t1 = influxConnectionPool.getInfluxConnection("test1", true);
        InfluxConnection t2 = influxConnectionPool.getInfluxConnection("test2", true);

        Map<String, String> tags = new HashMap<>();
        tags.put("key1", "1");
        Map<String, Object> tags2 = new HashMap<>();
        tags2.put("value", "1");

        for(int i = 0; i < 10; i++){
            t1.insert("test1", tags, tags2, 0, null);
            t2.insert("test2", tags, tags2, 0, null);
        }
    }
}
