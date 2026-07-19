package com.globalpay.kafka.consumer;

import com.globalpay.kafka.event.TransactionCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TransactionEventConsumer {

    @KafkaListener(topics = "transaction-created", groupId = "global-pay-group")
    public void consumeTransactionCreatedEvent(TransactionCreatedEvent event) {
        log.info("Consumed TransactionCreatedEvent: {}", event.getTransactionRef());
        // Process transaction creation event
        // Send notification, update analytics, etc.
    }

    @KafkaListener(topics = "transaction-completed", groupId = "global-pay-group")
    public void consumeTransactionCompletedEvent(TransactionCreatedEvent event) {
        log.info("Consumed TransactionCompletedEvent: {}", event.getTransactionRef());
        // Process transaction completion event
        // Send confirmation notification, settle funds, etc.
    }
}
