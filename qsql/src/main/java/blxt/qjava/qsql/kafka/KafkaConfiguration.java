package blxt.qjava.qsql.kafka;

import blxt.qjava.autovalue.inter.Value;
import lombok.Data;

import java.util.Properties;


/**
 * 读取包含redis配置的属性文件，并提供一些使用{@link Properties}的实用方法.
 * @author 张家磊
 */
@Data
public class KafkaConfiguration {

    @Value("bootstrap.servers")
    String bootstrap_servers =  "localhost:9092";
    /** 指定消息key和消息体的编解码方式, org.apache.kafka.common.serialization.StringSerializer  */
    @Value("key.serializer")
    String KEY_SERIALIZER    =  "org.apache.kafka.common.serialization.StringSerializer";
    @Value("value.serializer")
     String VALUE_SERIALIZER  =  "org.apache.kafka.common.serialization.StringSerializer";
    /** 写入失败时，重试次数。当leader节点失效，一个repli节点会替代成为leader节点，此时可能出现写入失败，
     *  当retris为0时，produce不会重复。retirs重发，此时repli节点完全成为leader节点，不会产生消息丢失。 */
    @Value("producer.retries")
    int PRODUCER_RETRIES  =   3;
    /** 每次批量发送消息的数量,produce积累到一定数据，一次发送 */
    @Value("producer.batch_size")
    int PRODUCER_BATCH_SIZE =      100;
    /** produce积累数据一次发送，缓存大小达到buffer.memory就发送数据 */
    @Value("producer.buffer-memor")
    int producer_buffer_memory =    100;
    /** #procedure要求leader在考虑完成请求之前收到的确认数，用于控制发送记录在服务端的持久化，其值可以为如下：
     #acks = 0 如果设置为零，则生产者将不会等待来自服务器的任何确认，该记录将立即添加到套接字缓冲区并视为已发送。在这种情况下，无法保证服务器已收到记录，并且重试配置将不会生效（因为客户端通常不会知道任何故障），为每条记录返回的偏移量始终设置为-1。
     #acks = 1 这意味着leader会将记录写入其本地日志，但无需等待所有副本服务器的完全确认即可做出回应，在这种情况下，如果leader在确认记录后立即失败，但在将数据复制到所有的副本服务器之前，则记录将会丢失。
     #acks = all 这意味着leader将等待完整的同步副本集以确认记录，这保证了只要至少一个同步副本服务器仍然存活，记录就不会丢失，这是最强有力的保证，这相当于acks = -1的设置。
     #可以设置的值为：all, -1, 0, 1 */
    @Value("producer.acks")
    int PRODUCER_ACKS      =  0;
    /** 自定义主题分区规则 */
    @Value("partitioner.clas")
    String PARTITIONER_CLAS   = "partitioner.clas";

}
