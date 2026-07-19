package com.globalpay.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionCreatedEvent {
    private String transactionId;
    private String transactionRef;
    private String senderId;
    private String recipientId;
    private BigDecimal sendAmount;
    private String sendCurrency;
    private BigDecimal receiveAmount;
    private String receiveCurrency;
    private BigDecimal fee;
    private LocalDateTime createdAt;
}
