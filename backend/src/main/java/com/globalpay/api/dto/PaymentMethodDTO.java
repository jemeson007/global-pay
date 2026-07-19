package com.globalpay.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethodDTO {
    private String id;
    private String type;
    private String name;
    private Boolean isDefault;
    private Boolean isActive;
    private String bankName;
    private String accountHolderName;
    private String cardLastFourDigits;
    private String cardBrand;
    private String mobileNumber;
    private String mobileProvider;
    private LocalDateTime createdAt;
    private LocalDateTime verifiedAt;
}
