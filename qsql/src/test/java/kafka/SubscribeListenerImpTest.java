package kafka;

import blxt.qjava.qsql.kafka.KafkaConnection;
import blxt.qjava.qsql.kafka.SubscribeListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/8/15 17:02
 */
public class SubscribeListenerImpTest implements SubscribeListener {

    private static final @NotNull
    Logger log = LoggerFactory.getLogger(KafkaConnection.class);

    @Override
    public void onInbound(ConsumerRecords<String, String> records) {
        //

        for (ConsumerRecord<String, String> record : records) {
            log.info("topic:{},offset:{},消息:{}", record.topic(), record.offset(), record.value());
        }
    }
}
