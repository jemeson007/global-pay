package com.globalpay.api.controller;

import com.globalpay.api.dto.TransactionDTO;
import com.globalpay.api.request.CreateTransactionRequest;
import com.globalpay.service.TransactionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@Slf4j
@Tag(name = "Transactions", description = "Transaction management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(
            Authentication authentication,
            @Valid @RequestBody CreateTransactionRequest request) {
        String userId = authentication.getName();
        log.info("Creating transaction for user: {}", userId);
        TransactionDTO transaction = transactionService.createTransaction(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDTO> getTransaction(
            Authentication authentication,
            @PathVariable String transactionId) {
        String userId = authentication.getName();
        TransactionDTO transaction = transactionService.getTransaction(transactionId, userId);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping
    public ResponseEntity<Page<TransactionDTO>> getAllTransactions(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        String userId = authentication.getName();
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<TransactionDTO> transactions = transactionService.getUserTransactions(userId, pageable);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/sent")
    public ResponseEntity<Page<TransactionDTO>> getSentTransactions(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        String userId = authentication.getName();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<TransactionDTO> transactions = transactionService.getSentTransactions(userId, pageable);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/received")
    public ResponseEntity<Page<TransactionDTO>> getReceivedTransactions(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        String userId = authentication.getName();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<TransactionDTO> transactions = transactionService.getReceivedTransactions(userId, pageable);
        return ResponseEntity.ok(transactions);
    }
}
