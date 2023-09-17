package com.common.retryProcess.service;

import com.common.retryProcess.configuration.KafkaConsumerConfiguration;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.CooperativeStickyAssignor;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class RetryConsumerServiceImpl {

    @Autowired
    private KafkaConsumerConfiguration kafkaConsumerConfiguration;

    @Value("${spring.kafka.template.default-topic}")
    private String topic;
    public Consumer<String, String> createConsumer()
    {
        Map<String, Object> properties= new HashMap<>();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumerConfiguration.getBootstrapServers());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaConsumerConfiguration.getKeyDeserializer());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaConsumerConfiguration.getValueDeserializer());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerConfiguration.getAutoOffsetReset());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerConfiguration.getGroupId());
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaConsumerConfiguration.isEnableAutoCommit());
        properties.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, CooperativeStickyAssignor.class.getName());

        KafkaConsumer<String, String> kafkaConsumer= new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Arrays.asList(topic));
        return kafkaConsumer;
    }

}
