import blxt.qjava.qsql.influxdb.*;
import model.TaskBean;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexTest {


    public static void main(String[] args) throws Exception {

        InfluxDbQuerySQL influxDbQuerySQL = new InfluxDbQuerySQL();
        influxDbQuerySQL.setTable("/client/rule/demouser");
        influxDbQuerySQL.setTimeStart("time ");
        influxDbQuerySQL.setTimeEnd("now() - 5m");
        influxDbQuerySQL.build();

        InfluxBean influxBean = new InfluxBean("root", "zxcvbnm", "http://192.168.3.20:8086", "", "default", false);
        InfluxConnectionPool influxConnectionPool = InfluxConnectionPool.newInstance(influxBean);

     //   InfluxConnection t1 = influxConnectionPool.getInfluxConnection("system.rule.iot", true);

        // 获取数据库, 没有就创建
        InfluxConnection t2 = influxConnectionPool.getInfluxConnection("test2", true);

        // 制造数据
        Map<String, String> tags = new HashMap<>();
        tags.put("key1", "123");
       // tags.put("key2", "414");
        Map<String, Object> fields = new HashMap<>();
        fields.put("value", "12412");
        fields.put("value2", "12412");

        for(int i = 0; i < 10; i++){
            t2.insert("test1/1", tags, fields, 0, null);
        }

//        QueryResult queryResult = t2.query("SELECT * FROM \"test1/1\"");
//        QueryResultMapper queryResultMapper = new QueryResultMapper();
//        List<TaskBean> beans = queryResultMapper.toObject(queryResult, TaskBean.class);
//        System.out.printf("TaskBeans:%s\n", beans);
    }
}
