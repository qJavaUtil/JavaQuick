package kafka;

import com.qjava.qsql.kafka.KafkaConnection;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/8/9 21:54
 */
public class KafkaTest {
    public  String topic = "rule.user";//定义主题
    String msg = "{\"modelData\":{\"clientInformation\":{\"clientId\":\"d123123\",\"groupId\":\"p1\",\"tags\":\"g1\",\"userId\":\"user1\"},\"fields\":{\"temperature\":15,\"humidity\":58,\"active\":false,\"fengsu\":50},\"tags\":{\"devicekey\":\"dev132123\",\"productKey\":\"a1wsYOqfoAi\",\"user\":\"user1\",\"groupkey\":\"group1\"},\"topic\":\"/sys/data/p1/d123123/things/properts\"},\"ruleEntitry\":{\"empty\":false,\"identifier\":\"temperature\",\"name\":\"属性测试\",\"symbos\":\"notmore\",\"type\":1,\"value\":\"30\"}}";
    @Test
    public void put(){
        // 生产者配置
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "sql.zhangjialei.cn:9092");//kafka地址，多个地址用逗号分割
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        //properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "com.qjava.qsql.kafka.CidPartitioner"); // 自定义主题分区规则
        // 消费者配置
        Properties properties2 = new Properties();
        properties2.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "sql.zhangjialei.cn:9092");//kafka地址，多个地址用逗号分割
        properties2.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties2.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties2.put(ConsumerConfig.GROUP_ID_CONFIG, "user.test"); // 消费者组id


        // 消费者
        KafkaConnection kafkaConnection_c = new KafkaConnection(properties2);
        kafkaConnection_c.buildConsumer();
        kafkaConnection_c.subscribe(topic);

        Runnable runnable = kafkaConnection_c.makeListener(new SubscribeListenerImpTest(), 1000);

        new Thread(runnable).start();


        // 生产者
        KafkaConnection KafkaConnection = new KafkaConnection(properties);
        KafkaConnection.buildProducer();

       // KafkaManager.put(topic, "测试2");
        try {
            KafkaConnection.putSync(topic, msg);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
