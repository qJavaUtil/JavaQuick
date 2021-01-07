package blxt.qjava.qsql.kafka;

import kafka.utils.VerifiableProperties;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/8/17 10:03
 */
public class CidPartitioner implements Partitioner {

    public CidPartitioner(VerifiableProperties props) {
        //注意 ： 构造函数的函数体没有东西，但是不能没有构造函数
    }


    @Override
    public int partition(String topic, Object o, byte[] bytes, Object o1, byte[] bytes1, Cluster cluster) {
        // 为主题分区
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        // 分区大小
        int partitionNum = partitions.size();
        // 随机一下
        Random random = new Random();

        int partition = random.nextInt(partitionNum);

        return partition;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
