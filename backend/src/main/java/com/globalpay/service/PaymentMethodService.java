package com.globalpay.service;

import com.globalpay.api.dto.PaymentMethodDTO;
import com.globalpay.entity.PaymentMethod;
import com.globalpay.entity.User;
import com.globalpay.exception.GlobalPayException;
import com.globalpay.exception.ResourceNotFoundException;
import com.globalpay.repository.PaymentMethodRepository;
import com.globalpay.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final UserRepository userRepository;

    public PaymentMethodService(PaymentMethodRepository paymentMethodRepository, UserRepository userRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.userRepository = userRepository;
    }

    public PaymentMethodDTO addPaymentMethod(String userId, PaymentMethodDTO request) {
        log.info("Adding payment method for user: {}", userId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        PaymentMethod paymentMethod = PaymentMethod.builder()
            .user(user)
            .type(PaymentMethod.PaymentMethodType.valueOf(request.getType()))
            .name(request.getName())
            .isDefault(request.getIsDefault() != null && request.getIsDefault())
            .isActive(true)
            .bankName(request.getBankName())
            .accountHolderName(request.getAccountHolderName())
            .cardLastFourDigits(request.getCardLastFourDigits())
            .cardBrand(request.getCardBrand())
            .mobileNumber(request.getMobileNumber())
            .mobileProvider(request.getMobileProvider())
            .build();

        // If setting as default, unset other defaults
        if (paymentMethod.getIsDefault()) {
            paymentMethodRepository.findDefaultByUserId(userId).ifPresent(pm -> {
                pm.setIsDefault(false);
                paymentMethodRepository.save(pm);
            });
        }

        paymentMethod = paymentMethodRepository.save(paymentMethod);
        log.info("Payment method added: {}", paymentMethod.getId());

        return mapToDTO(paymentMethod);
    }

    public List<PaymentMethodDTO> getUserPaymentMethods(String userId) {
        return paymentMethodRepository.findByUserId(userId).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public PaymentMethodDTO getPaymentMethod(String methodId, String userId) {
        PaymentMethod method = paymentMethodRepository.findById(methodId)
            .orElseThrow(() -> new ResourceNotFoundException("Payment method not found"));

        if (!method.getUser().getId().equals(userId)) {
            throw new GlobalPayException("Unauthorized access to payment method");
        }

        return mapToDTO(method);
    }

    public PaymentMethodDTO updatePaymentMethod(String methodId, String userId, PaymentMethodDTO request) {
        PaymentMethod method = paymentMethodRepository.findById(methodId)
            .orElseThrow(() -> new ResourceNotFoundException("Payment method not found"));

        if (!method.getUser().getId().equals(userId)) {
            throw new GlobalPayException("Unauthorized access to payment method");
        }

        if (request.getName() != null) {
            method.setName(request.getName());
        }

        if (request.getIsDefault() != null && request.getIsDefault()) {
            paymentMethodRepository.findDefaultByUserId(userId).ifPresent(pm -> {
                pm.setIsDefault(false);
                paymentMethodRepository.save(pm);
            });
            method.setIsDefault(true);
        }

        method = paymentMethodRepository.save(method);
        return mapToDTO(method);
    }

    public void deletePaymentMethod(String methodId, String userId) {
        PaymentMethod method = paymentMethodRepository.findById(methodId)
            .orElseThrow(() -> new ResourceNotFoundException("Payment method not found"));

        if (!method.getUser().getId().equals(userId)) {
            throw new GlobalPayException("Unauthorized access to payment method");
        }

        paymentMethodRepository.delete(method);
        log.info("Payment method deleted: {}", methodId);
    }

    private PaymentMethodDTO mapToDTO(PaymentMethod method) {
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
