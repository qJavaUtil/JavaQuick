package blxt.qjava.qsql.kafka;


import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.PartitionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * kafka连接
 * 作为生产者 : buildProducer()
 * 作为消费者 : buildConsumer()
 * @Author: Zhang.Jialei
 * @Date: 2020/8/9 21:57
 */
public class KafkaConnection {

    private static final Logger log = LoggerFactory.getLogger(KafkaConnection.class);


    Properties properties = null;
    /** 第一个类型代表key的类型，第二个代表消息的类型 */
    KafkaProducer<String, String> kafkaProducer = null;
    KafkaConsumer<String, String> kafkaConsumer = null;

    public KafkaConnection(Properties properties){
        this.properties = properties;
    }


    /**
     * 构建生产者
     */
    public KafkaConnection buildProducer(){
        if(kafkaProducer == null){
            try {
                kafkaProducer = new KafkaProducer<String, String>(properties);
            }catch (Exception e){
                kafkaProducer = null;
                e.printStackTrace();
                return null;
            }
        }
        return this;
    }

    /**
     * 构建消费者
     */
    public KafkaConnection buildConsumer(){
        if(kafkaConsumer == null){
            try {
                kafkaConsumer = new KafkaConsumer<String, String>(properties);
            }catch (Exception e){
                kafkaConsumer = null;
                e.printStackTrace();
                return null;
            }
        }
        return this;
    }

    /**
     * 推送
     * @param topic  主题
     * @param msg    消息
     */
    public Future<RecordMetadata> put(String topic, String msg) {
        ProducerRecord record = new ProducerRecord<String, String>(topic, msg);
        return kafkaProducer.send(record);
        // log.info("推送成成功{}",  record.partition());
    }

    /**
     * 同步推送
     * @param topic
     * @param key
     * @param msg
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public RecordMetadata putSync(String topic, String key, String msg) throws ExecutionException, InterruptedException {
        ProducerRecord<String,String> record = new ProducerRecord<>(topic, key, msg);

        return kafkaProducer.send(record).get();
        //log.info("时间戳{}，主题{}，分区{}，位移{} ",  result.timestamp(), record.topic(), result.partition(), result.offset());
    }

    /**
     * 同步推送
     * @param topic
     * @param msg
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public RecordMetadata putSync(String topic, String msg) throws ExecutionException, InterruptedException {
        ProducerRecord<String,String> record = new ProducerRecord<>(topic, msg);

        return kafkaProducer.send(record).get();
        //log.info("时间戳{}，主题{}，分区{}，位移{} ",  result.timestamp(), record.topic(), result.partition(), result.offset());
    }

    /**
     * 订阅
     * @param topic
     */
    public void subscribe(String topic){
        kafkaConsumer.subscribe(Collections.singletonList(topic));
    }

    /**
     * 创建订阅监听任务, low level API
     * @param subscribeListener kafkaConsumer订阅监听 ,NotNull
     * @param timeoutMs         poll 超时,毫秒
     * @return  订阅线程,Runnable
     */
    public Runnable makeListener(SubscribeListener subscribeListener, long timeoutMs){
        Runnable runnable = new Runnable(){

            @Override
            public void run(){
                while (true) {
                    // log.info("等待接收");
                    ConsumerRecords<String, String> records = null;
                    try {
                        records = kafkaConsumer.poll(timeoutMs);
                    }catch (Exception e){
                    //    log.info("接收异常");
                        e.printStackTrace();
                    }
                    // log.info("接收成功:{}", records.count());
                    subscribeListener.onInbound(records);
                }
            }
        };

        return runnable;
    }

    /**
     * 获取Producer的PartitionInfo集合
     * @param top
     * @return
     */
    public List<PartitionInfo> getPartitionInfo(String top){

       return kafkaProducer.partitionsFor(top);
    }

    /**
     * 关闭
     */
    public void close(){
        if(kafkaProducer != null) {
            kafkaProducer.close();
        }

        if(kafkaConsumer != null){
            kafkaConsumer.close();
        }
    }
}