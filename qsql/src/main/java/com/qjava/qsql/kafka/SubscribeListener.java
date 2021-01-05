package com.qjava.qsql.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecords;

/**
 * 卡夫卡订阅监听
 * @Author: Zhang.Jialei
 * @Date: 2020/8/15 16:55
 */
public interface SubscribeListener {
    /**
     * 订阅入站
     * @param records
     */
    void onInbound(ConsumerRecords<String, String> records);
}
