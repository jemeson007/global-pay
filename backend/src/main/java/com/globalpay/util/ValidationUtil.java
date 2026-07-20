package com.globalpay.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
@Slf4j
public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$");
    
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^\\+?[1-9]\\d{1,14}$"); // E.164 format
    
    private static final Pattern IBAN_PATTERN = 
        Pattern.compile("^[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}$");

    public boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public boolean isValidPhoneNumber(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    public boolean isValidIban(String iban) {
        return iban != null && IBAN_PATTERN.matcher(iban).matches();
    }

    public boolean isValidCurrencyCode(String code) {
        return code != null && code.length() == 3 && code.matches("[A-Z]{3}");
    }

    public boolean isValidAmount(java.math.BigDecimal amount) {
        return amount != null && amount.compareTo(java.math.BigDecimal.ZERO) > 0;
    }
}
