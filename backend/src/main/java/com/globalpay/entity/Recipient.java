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
@Table(name = "recipients", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_recipient_email", columnList = "email")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(columnDefinition = "VARCHAR(255)")
    private String phoneNumber;

    @Column(nullable = false, columnDefinition = "VARCHAR(3)")
    private String currency;

    @Column(nullable = false)
    private String bankName;

    @Column(columnDefinition = "VARCHAR(255)")
    private String accountHolderName;

    @Column(columnDefinition = "VARCHAR(255)")
    private String accountNumber; // Encrypted

    @Column(columnDefinition = "VARCHAR(255)")
    private String routingNumber; // Encrypted

    @Column(columnDefinition = "VARCHAR(255)")
    private String iban; // Encrypted

    @Column(columnDefinition = "VARCHAR(255)")
    private String swiftCode;

    @Column(nullable = false, columnDefinition = "VARCHAR(2)")
    private String country;

    @Column(columnDefinition = "VARCHAR(255)")
    private String city;

    @Column(columnDefinition = "VARCHAR(255)")
    private String address;

    @Column(columnDefinition = "JSONB")
    private String metadata;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean isActive;

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime lastUsedAt;
}
