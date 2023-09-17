package com.common.retryProcess.consumer;

import com.common.retryProcess.configuration.EmailDetails;
import com.common.retryProcess.service.NotificationService;
import com.common.retryProcess.service.RetryConsumerServiceImpl;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledRetryConsumer {

    Logger logger = LoggerFactory.getLogger(ScheduledRetryConsumer.class);
    private Consumer<String, String> consumer = null;

    @Autowired
    private RetryConsumerServiceImpl retryConsumerService;

    @Autowired
    private EmailDetails emailDetails;
    @Autowired
    private NotificationService notificationService;

    @Scheduled(fixedRate = 120000)
    public void consume() {
        logger.info("Starting Consumer");

        if (consumer == null) {
            consumer = retryConsumerService.createConsumer();
        }
        ConsumerRecords<String, String> consumerRecords = consumer.poll(500l);


        logger.info("Total Records in the Kafka: {}", consumerRecords.count());

        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
            logger.info("Record Consumed from the topic: {}", consumerRecord.value().toString());
            notificationService.sendEmail(consumerRecord.value().toString(), emailDetails);
        }
    }
}
