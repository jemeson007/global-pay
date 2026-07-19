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
@Table(name = "exchange_rates", indexes = {
    @Index(name = "idx_currency_pair", columnList = "from_currency, to_currency"),
    @Index(name = "idx_updated_at", columnList = "updated_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, columnDefinition = "VARCHAR(3)")
    private String fromCurrency;

    @Column(nullable = false, columnDefinition = "VARCHAR(3)")
    private String toCurrency;

    @Column(nullable = false, columnDefinition = "NUMERIC(19,6)")
    private BigDecimal rate;

    @Column(nullable = false, columnDefinition = "NUMERIC(19,6)")
    private BigDecimal midMarketRate;

    @Column(columnDefinition = "NUMERIC(5,2)")
    private BigDecimal margin; // Our margin percentage

    @Column(nullable = false, columnDefinition = "NUMERIC(19,6)")
    private BigDecimal ourRate; // Rate with margin applied

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime rateTimestamp;
}
