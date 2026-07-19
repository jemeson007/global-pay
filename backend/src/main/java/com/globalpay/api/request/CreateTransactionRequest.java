package com.globalpay.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionRequest {
    @NotBlank(message = "Recipient ID is required")
    private String recipientId;

    @NotNull(message = "Send amount is required")
    @Positive(message = "Send amount must be greater than 0")
    private BigDecimal sendAmount;

    @NotBlank(message = "Send currency is required")
    private String sendCurrency;

    @NotBlank(message = "Payment method ID is required")
    private String paymentMethodId;

    private String description;
}
