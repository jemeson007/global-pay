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
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_email", columnList = "email", unique = true),
    @Index(name = "idx_phone", columnList = "phone_number", unique = true),
    @Index(name = "idx_kyc_status", columnList = "kyc_status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private KycStatus kycStatus;

    @Column(columnDefinition = "JSONB")
    private String kycData;

    @Column(nullable = false, columnDefinition = "NUMERIC(19,2) DEFAULT 0")
    private BigDecimal accountBalance;

    @Column(nullable = false, columnDefinition = "VARCHAR(3) DEFAULT 'USD'")
    private String defaultCurrency;

    @Column(nullable = false)
    private Boolean emailVerified;

    @Column(nullable = false)
    private Boolean phoneVerified;

    @Column(nullable = false)
    private Boolean twoFactorEnabled;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean active;

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime lastLoginAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PaymentMethod> paymentMethods = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Recipient> recipients = new HashSet<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private Set<Transaction> sentTransactions = new HashSet<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private Set<Transaction> receivedTransactions = new HashSet<>();

    public enum KycStatus {
        PENDING,
        IN_REVIEW,
        VERIFIED,
        REJECTED,
        EXPIRED
    }
}
