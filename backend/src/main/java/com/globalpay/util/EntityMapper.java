package com.globalpay.util;

import com.globalpay.api.dto.*;
import com.globalpay.entity.*;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper {

    public UserDTO toUserDTO(User user) {
        if (user == null) return null;
        return UserDTO.builder()
            .id(user.getId())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .phoneNumber(user.getPhoneNumber())
            .kycStatus(user.getKycStatus().toString())
            .accountBalance(user.getAccountBalance())
            .defaultCurrency(user.getDefaultCurrency())
            .emailVerified(user.getEmailVerified())
            .phoneVerified(user.getPhoneVerified())
            .twoFactorEnabled(user.getTwoFactorEnabled())
            .createdAt(user.getCreatedAt())
            .lastLoginAt(user.getLastLoginAt())
            .build();
    }

    public TransactionDTO toTransactionDTO(Transaction transaction) {
        if (transaction == null) return null;
        return TransactionDTO.builder()
            .id(transaction.getId())
            .transactionRef(transaction.getTransactionRef())
            .senderId(transaction.getSender().getId())
            .receiverId(transaction.getReceiver() != null ? transaction.getReceiver().getId() : null)
            .sendAmount(transaction.getSendAmount())
            .sendCurrency(transaction.getSendCurrency())
            .receiveAmount(transaction.getReceiveAmount())
            .receiveCurrency(transaction.getReceiveCurrency())
            .exchangeRate(transaction.getExchangeRate())
            .fee(transaction.getFee())
            .discount(transaction.getDiscount())
            .status(transaction.getStatus().toString())
            .type(transaction.getType().toString())
            .description(transaction.getDescription())
            .createdAt(transaction.getCreatedAt())
            .completedAt(transaction.getCompletedAt())
            .failureReason(transaction.getFailureReason())
            .build();
    }

    public PaymentMethodDTO toPaymentMethodDTO(PaymentMethod method) {
        if (method == null) return null;
        return PaymentMethodDTO.builder()
            .id(method.getId())
            .type(method.getType().toString())
            .name(method.getName())
            .isDefault(method.getIsDefault())
            .isActive(method.getIsActive())
            .bankName(method.getBankName())
            .accountHolderName(method.getAccountHolderName())
            .cardLastFourDigits(method.getCardLastFourDigits())
            .cardBrand(method.getCardBrand())
            .mobileNumber(method.getMobileNumber())
            .mobileProvider(method.getMobileProvider())
            .createdAt(method.getCreatedAt())
            .verifiedAt(method.getVerifiedAt())
            .build();
    }
}
