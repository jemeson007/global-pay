package com.globalpay.kafka.producer;

import com.globalpay.config.KafkaConfig;
import com.globalpay.kafka.event.TransactionCreatedEvent;
import com.globalpay.kafka.event.TransactionCompletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TransactionEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public TransactionEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void produceTransactionCreatedEvent(TransactionCreatedEvent event) {
        log.info("Publishing TransactionCreatedEvent for transaction: {}", event.getTransactionRef());
        kafkaTemplate.send(KafkaConfig.TRANSACTION_CREATED_TOPIC, event.getTransactionRef(), event);
    }

    public void produceTransactionCompletedEvent(TransactionCompletedEvent event) {
        log.info("Publishing TransactionCompletedEvent for transaction: {}", event.getTransactionRef());
        kafkaTemplate.send(KafkaConfig.TRANSACTION_COMPLETED_TOPIC, event.getTransactionRef(), event);
    }
}
