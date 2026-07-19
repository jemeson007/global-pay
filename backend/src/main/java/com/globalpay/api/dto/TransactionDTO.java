package com.globalpay.api.dto;

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
public class TransactionDTO {
    private String id;
    private String transactionRef;
    private String senderId;
    private String receiverId;
    private BigDecimal sendAmount;
    private String sendCurrency;
    private BigDecimal receiveAmount;
    private String receiveCurrency;
    private BigDecimal exchangeRate;
    private BigDecimal fee;
    private BigDecimal discount;
    private String status;
    private String type;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private String failureReason;
}
