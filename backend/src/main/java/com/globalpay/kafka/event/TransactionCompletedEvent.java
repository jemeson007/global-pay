package com.globalpay.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionCompletedEvent {
    private String transactionId;
    private String transactionRef;
    private String senderId;
    private String recipientId;
    private String status;
    private LocalDateTime completedAt;
}
