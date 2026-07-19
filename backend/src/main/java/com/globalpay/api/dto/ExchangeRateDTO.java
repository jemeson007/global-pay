package com.globalpay.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeRateDTO {
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal rate;
    private BigDecimal midMarketRate;
    private BigDecimal margin;
    private BigDecimal ourRate;
}
