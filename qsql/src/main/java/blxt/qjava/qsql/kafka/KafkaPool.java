package blxt.qjava.qsql.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/8/17 14:05
 */
public class KafkaPool {
    /** 单例 */
    static KafkaPool instance = null;
    /** 默认kafka链接属性 */
    Properties properties = null;
    /** 默认kafka链接配置 */
    KafkaConfiguration configuration = null;
    Map<String , KafkaConnection> connectionMap = new HashMap<>();


    public static KafkaPool newInstance(KafkaConfiguration configuration){
        if(instance != null){
            return instance;
        }
        instance = new KafkaPool(configuration);
        return instance;
    }

    public static KafkaPool getInstance(){
        return instance;
    }

    /**
     * 从配置文件,构造链接属性
     * @param configuration
     */
    public KafkaPool(KafkaConfiguration configuration){
        this.configuration = configuration;
        properties = new Properties();
        // kafka地址，多个地址用逗号分割
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,configuration.getBootstrap_servers());
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    }

    /**
     * 添加一个链接
     * @param key
     * @return
     */
    public KafkaConnection addConnection(String key){
        return addConnection(key, properties);
    }

    /**
     * 从指定配置添加一个链接
     * @param key
     * @return
     */
    public KafkaConnection addConnection(String key, Properties properties){
        KafkaConnection connection = connectionMap.get(key);
        if(connection == null){
            connection = new KafkaConnection(properties);
            connectionMap.put(key, connection);
        }
        return connection;
    }


    /**
     * 添加一个KafkaProducerConnection
     * @param key
     * @return
     */
    public KafkaProducerConnection addProducer(String key){
        return addProducer(key, properties);
    }

    /**
     * 从指定配置添加一个KafkaProducerConnection
     * @param key
     * @return
     */
    public KafkaProducerConnection addProducer(String key, Properties properties){
        KafkaConnection connection = connectionMap.get(key);
        if(connection == null){
            connection = new KafkaProducerConnection(properties);
            connectionMap.put(key, connection);
        }
        return (KafkaProducerConnection)connection;
    }

    /**
     * 添加一个KafkaProducerConnection
     * @param key
     * @return
     */
    public KafkaConsumerConnection addConsumer(String key){
        return addConsumer(key, properties);
    }

    /**
     * 从指定配置添加一个KafkaProducerConnection
     * @param key
     * @return
     */
    public KafkaConsumerConnection addConsumer(String key, Properties properties){
        KafkaConnection connection = connectionMap.get(key);
        if(connection == null){
            connection = new KafkaConsumerConnection(properties);
            connectionMap.put(key, connection);
        }
        return (KafkaConsumerConnection)connection;
    }

    /**
     * 获取一个链接
     * @param key
     * @return
     */
    public KafkaConnection getConnection(String key){
        KafkaConnection connection = connectionMap.get(key);
        if(connection != null){
            return connection ;
        }
        return addConnection(key);
    }

    /**
     * 获取一个KafkaProducerConnection
     * @param key
     * @return
     */
    public KafkaProducerConnection getProducer(String key){
        KafkaProducerConnection connection = (KafkaProducerConnection)connectionMap.get(key);
        if(connection != null){
            return connection ;
        }
        return addProducer(key);
    }

    /**
     * 获取一个 KafkaProducerConnection
     * @param key
     * @return
     */
    public KafkaConsumerConnection getConsumer(String key){
        KafkaConsumerConnection connection = (KafkaConsumerConnection)connectionMap.get(key);
        if(connection != null){
            return connection ;
        }
        return addConsumer(key);
    }

}
