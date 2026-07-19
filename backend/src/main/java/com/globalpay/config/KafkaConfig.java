package com.globalpay.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    public static final String TRANSACTION_CREATED_TOPIC = "transaction-created";
    public static final String TRANSACTION_COMPLETED_TOPIC = "transaction-completed";
    public static final String PAYMENT_PROCESSING_TOPIC = "payment-processing";
    public static final String USER_NOTIFICATION_TOPIC = "user-notification";
    public static final String EXCHANGE_RATE_UPDATE_TOPIC = "exchange-rate-update";

    @Bean
    public NewTopic transactionCreatedTopic() {
        return TopicBuilder.name(TRANSACTION_CREATED_TOPIC)
            .partitions(3)
            .replicas(1)
            .build();
    }

    @Bean
    public NewTopic transactionCompletedTopic() {
        return TopicBuilder.name(TRANSACTION_COMPLETED_TOPIC)
            .partitions(3)
            .replicas(1)
            .build();
    }

    @Bean
    public NewTopic paymentProcessingTopic() {
        return TopicBuilder.name(PAYMENT_PROCESSING_TOPIC)
            .partitions(3)
            .replicas(1)
            .build();
    }

    @Bean
    public NewTopic userNotificationTopic() {
        return TopicBuilder.name(USER_NOTIFICATION_TOPIC)
            .partitions(2)
            .replicas(1)
            .build();
    }

    @Bean
    public NewTopic exchangeRateUpdateTopic() {
        return TopicBuilder.name(EXCHANGE_RATE_UPDATE_TOPIC)
            .partitions(1)
            .replicas(1)
            .build();
    }
}
