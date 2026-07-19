package com.globalpay.api.controller;

import com.globalpay.api.dto.PaymentMethodDTO;
import com.globalpay.service.PaymentMethodService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment-methods")
@Slf4j
@Tag(name = "Payment Methods", description = "Payment method management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    public PaymentMethodController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @PostMapping
    public ResponseEntity<PaymentMethodDTO> addPaymentMethod(
            Authentication authentication,
            @Valid @RequestBody PaymentMethodDTO request) {
        String userId = authentication.getName();
        log.info("Adding payment method for user: {}", userId);
        PaymentMethodDTO paymentMethod = paymentMethodService.addPaymentMethod(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentMethod);
    }

    @GetMapping
    public ResponseEntity<List<PaymentMethodDTO>> getPaymentMethods(Authentication authentication) {
        String userId = authentication.getName();
        List<PaymentMethodDTO> methods = paymentMethodService.getUserPaymentMethods(userId);
        return ResponseEntity.ok(methods);
    }

    @GetMapping("/{methodId}")
    public ResponseEntity<PaymentMethodDTO> getPaymentMethod(
            Authentication authentication,
            @PathVariable String methodId) {
        String userId = authentication.getName();
        PaymentMethodDTO method = paymentMethodService.getPaymentMethod(methodId, userId);
        return ResponseEntity.ok(method);
    }

    @PutMapping("/{methodId}")
    public ResponseEntity<PaymentMethodDTO> updatePaymentMethod(
            Authentication authentication,
            @PathVariable String methodId,
            @Valid @RequestBody PaymentMethodDTO request) {
        String userId = authentication.getName();
        PaymentMethodDTO method = paymentMethodService.updatePaymentMethod(methodId, userId, request);
        return ResponseEntity.ok(method);
    }

    @DeleteMapping("/{methodId}")
    public ResponseEntity<Void> deletePaymentMethod(
            Authentication authentication,
            @PathVariable String methodId) {
        String userId = authentication.getName();
        paymentMethodService.deletePaymentMethod(methodId, userId);
        return ResponseEntity.noContent().build();
    }
}
