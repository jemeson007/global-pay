package com.globalpay.repository;

import com.globalpay.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, String> {
    @Query("SELECT er FROM ExchangeRate er WHERE er.fromCurrency = :fromCurrency AND er.toCurrency = :toCurrency ORDER BY er.updatedAt DESC LIMIT 1")
    Optional<ExchangeRate> findLatestRate(@Param("fromCurrency") String fromCurrency, @Param("toCurrency") String toCurrency);
}
