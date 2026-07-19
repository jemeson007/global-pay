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
public class RecipientDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String currency;
    private String bankName;
    private String accountHolderName;
    private String swiftCode;
    private String country;
    private String city;
    private String address;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime lastUsedAt;
}
