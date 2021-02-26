package blxt.qjava.qsql.kafka;


import java.util.Properties;

/**
 * kafka连接
 * 作为生产者 : buildProducer()
 * 作为消费者 : buildConsumer()
 * @Author: Zhang.Jialei
 * @Date: 2020/8/9 21:57
 */
public class KafkaConsumerConnection extends KafkaConnection{

    public KafkaConsumerConnection(Properties properties){
        super(properties);
        buildConsumer();
    }

}