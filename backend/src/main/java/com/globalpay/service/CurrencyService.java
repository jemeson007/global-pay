package com.globalpay.service;

import com.globalpay.api.dto.ExchangeRateDTO;
import com.globalpay.entity.ExchangeRate;
import com.globalpay.exception.ResourceNotFoundException;
import com.globalpay.repository.ExchangeRateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@Slf4j
@Transactional
public class CurrencyService {

    private final ExchangeRateRepository exchangeRateRepository;
    private static final BigDecimal DEFAULT_MARGIN = new BigDecimal("0.02"); // 2% margin

    public CurrencyService(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @Cacheable(value = "exchangeRates", key = "#fromCurrency + '-' + #toCurrency")
    public ExchangeRateDTO getExchangeRate(String fromCurrency, String toCurrency) {
        log.info("Fetching exchange rate from {} to {}", fromCurrency, toCurrency);

        ExchangeRate rate = exchangeRateRepository.findLatestRate(fromCurrency, toCurrency)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Exchange rate not found for " + fromCurrency + "/" + toCurrency
            ));

        return mapToDTO(rate);
    }

    public ExchangeRateDTO updateExchangeRate(String fromCurrency, String toCurrency, BigDecimal midMarketRate) {
        log.info("Updating exchange rate from {} to {} with rate {}", fromCurrency, toCurrency, midMarketRate);

        BigDecimal ourRate = calculateOurRate(midMarketRate, DEFAULT_MARGIN);

        ExchangeRate rate = ExchangeRate.builder()
            .fromCurrency(fromCurrency)
            .toCurrency(toCurrency)
            .rate(ourRate)
            .midMarketRate(midMarketRate)
            .margin(DEFAULT_MARGIN)
            .ourRate(ourRate)
            .rateTimestamp(LocalDateTime.now())
            .build();

        rate = exchangeRateRepository.save(rate);
        return mapToDTO(rate);
    }

    public BigDecimal convertCurrency(String fromCurrency, String toCurrency, BigDecimal amount) {
        ExchangeRateDTO rate = getExchangeRate(fromCurrency, toCurrency);
        return amount.multiply(rate.getOurRate()).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateFee(BigDecimal amount, String currency) {
        // Base fee: 1% of transaction amount with minimum fee of $1
        BigDecimal baseFee = amount.multiply(new BigDecimal("0.01")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal minimumFee = new BigDecimal("1.00");
        return baseFee.max(minimumFee);
    }

    private BigDecimal calculateOurRate(BigDecimal midMarketRate, BigDecimal margin) {
        return midMarketRate.multiply(BigDecimal.ONE.add(margin)).setScale(6, RoundingMode.HALF_UP);
    }

    private ExchangeRateDTO mapToDTO(ExchangeRate rate) {
        return ExchangeRateDTO.builder()
            .fromCurrency(rate.getFromCurrency())
            .toCurrency(rate.getToCurrency())
            .rate(rate.getRate())
            .midMarketRate(rate.getMidMarketRate())
            .margin(rate.getMargin())
            .ourRate(rate.getOurRate())
            .build();
    }
}
