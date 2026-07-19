package com.globalpay.api.controller;

import com.globalpay.api.dto.ExchangeRateDTO;
import com.globalpay.service.CurrencyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/currencies")
@Slf4j
@Tag(name = "Currencies", description = "Exchange rate and currency conversion endpoints")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/rates")
    public ResponseEntity<ExchangeRateDTO> getExchangeRate(
            @RequestParam String from,
            @RequestParam String to) {
        log.info("Fetching exchange rate from {} to {}", from, to);
        ExchangeRateDTO rate = currencyService.getExchangeRate(from, to);
        return ResponseEntity.ok(rate);
    }

    @PostMapping("/convert")
    public ResponseEntity<BigDecimal> convertCurrency(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam BigDecimal amount) {
        log.info("Converting {} {} to {}", amount, from, to);
        BigDecimal converted = currencyService.convertCurrency(from, to, amount);
        return ResponseEntity.ok(converted);
    }

    @GetMapping("/fee")
    public ResponseEntity<BigDecimal> calculateFee(
            @RequestParam BigDecimal amount,
            @RequestParam String currency) {
        log.info("Calculating fee for {} {}", amount, currency);
        BigDecimal fee = currencyService.calculateFee(amount, currency);
        return ResponseEntity.ok(fee);
    }
}
