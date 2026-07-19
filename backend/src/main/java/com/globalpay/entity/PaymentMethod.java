package com.globalpay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_methods", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_type", columnList = "type")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethodType type;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isDefault;

    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean isActive;

    // Bank account details (encrypted)
    @Column(columnDefinition = "VARCHAR(255)")
    private String bankName;

    @Column(columnDefinition = "VARCHAR(255)")
    private String accountHolderName;

    @Column(columnDefinition = "VARCHAR(255)")
    private String accountNumber; // Encrypted

    @Column(columnDefinition = "VARCHAR(255)")
    private String routingNumber; // Encrypted

    @Column(columnDefinition = "VARCHAR(255)")
    private String iban; // Encrypted

    // Card details (encrypted)
    @Column(columnDefinition = "VARCHAR(255)")
    private String cardLastFourDigits;

    @Column(columnDefinition = "VARCHAR(255)")
    private String cardBrand;

    @Column(columnDefinition = "VARCHAR(255)")
    private String cardHolderName;

    @Column(columnDefinition = "VARCHAR(255)")
    private String expiryDate; // Encrypted

    // Mobile money details
    @Column(columnDefinition = "VARCHAR(255)")
    private String mobileNumber; // Encrypted

    @Column(columnDefinition = "VARCHAR(255)")
    private String mobileProvider;

    @Column(columnDefinition = "JSONB")
    private String metadata;

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime verifiedAt;

    public enum PaymentMethodType {
        BANK_ACCOUNT,
        DEBIT_CARD,
        CREDIT_CARD,
        MOBILE_MONEY,
        DIGITAL_WALLET,
        CRYPTOCURRENCY
    }
}
