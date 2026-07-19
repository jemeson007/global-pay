package com.globalpay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions", indexes = {
    @Index(name = "idx_transaction_ref", columnList = "transaction_ref", unique = true),
    @Index(name = "idx_sender_id", columnList = "sender_id"),
    @Index(name = "idx_receiver_id", columnList = "receiver_id"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String transactionRef;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Column(nullable = false, columnDefinition = "NUMERIC(19,2)")
    private BigDecimal sendAmount;

    @Column(nullable = false, columnDefinition = "VARCHAR(3)")
    private String sendCurrency;

    @Column(nullable = false, columnDefinition = "NUMERIC(19,2)")
    private BigDecimal receiveAmount;

    @Column(nullable = false, columnDefinition = "VARCHAR(3)")
    private String receiveCurrency;

    @Column(nullable = false, columnDefinition = "NUMERIC(19,4)")
    private BigDecimal exchangeRate;

    @Column(nullable = false, columnDefinition = "NUMERIC(19,2)")
    private BigDecimal fee;

    @Column(columnDefinition = "NUMERIC(19,2)")
    private BigDecimal discount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "JSONB")
    private String metadata;

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime completedAt;

    private String failureReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    public enum TransactionStatus {
        PENDING,
        PROCESSING,
        COMPLETED,
        FAILED,
        CANCELLED,
        REFUNDED
    }

    public enum TransactionType {
        TRANSFER,
        PAYMENT,
        REFUND,
        TOPUP
    }
}
