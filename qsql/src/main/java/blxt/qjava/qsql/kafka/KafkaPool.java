package blxt.qjava.qsql.kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/8/17 14:05
 */
public class KafkaPool {
    static KafkaPool instance = null;
    static Map<String, KafkaConnection> kafkaConnectionMap = null;

    static {
        instance = new KafkaPool();
        kafkaConnectionMap = new HashMap<>();
    }



    public static KafkaPool getInstance(){
        return instance;
    }

    public KafkaConnection add(String name, Properties properties){
        KafkaConnection connection = kafkaConnectionMap.get(name);
        if(connection == null){
            connection = add(name, new KafkaConnection(properties));
        }

        return connection;
    }

    public KafkaConnection add(String name, KafkaConnection connection){
        kafkaConnectionMap.put(name, connection);
        return connection;
    }

    public KafkaConnection get(String name){
        KafkaConnection connection = kafkaConnectionMap.get(name);

        return connection;
    }

}
