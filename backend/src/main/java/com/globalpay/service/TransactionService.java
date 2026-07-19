package com.globalpay.service;

import com.globalpay.api.dto.TransactionDTO;
import com.globalpay.api.request.CreateTransactionRequest;
import com.globalpay.entity.PaymentMethod;
import com.globalpay.entity.Recipient;
import com.globalpay.entity.Transaction;
import com.globalpay.entity.User;
import com.globalpay.exception.GlobalPayException;
import com.globalpay.exception.ResourceNotFoundException;
import com.globalpay.repository.PaymentMethodRepository;
import com.globalpay.repository.RecipientRepository;
import com.globalpay.repository.TransactionRepository;
import com.globalpay.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final RecipientRepository recipientRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final CurrencyService currencyService;

    public TransactionService(TransactionRepository transactionRepository,
                             UserRepository userRepository,
                             RecipientRepository recipientRepository,
                             PaymentMethodRepository paymentMethodRepository,
                             CurrencyService currencyService) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.recipientRepository = recipientRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.currencyService = currencyService;
    }

    public TransactionDTO createTransaction(String senderId, CreateTransactionRequest request) {
        log.info("Creating transaction for user {} to recipient {}", senderId, request.getRecipientId());

        User sender = userRepository.findById(senderId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Recipient recipient = recipientRepository.findByIdAndUserId(request.getRecipientId(), senderId)
            .orElseThrow(() -> new ResourceNotFoundException("Recipient not found"));

        PaymentMethod paymentMethod = paymentMethodRepository.findById(request.getPaymentMethodId())
            .orElseThrow(() -> new ResourceNotFoundException("Payment method not found"));

        if (!paymentMethod.getIsActive()) {
            throw new GlobalPayException("Payment method is not active");
        }

        // Get exchange rate
        BigDecimal receiveAmount = currencyService.convertCurrency(
            request.getSendCurrency(),
            recipient.getCurrency(),
            request.getSendAmount()
        );

        // Calculate fee
        BigDecimal fee = currencyService.calculateFee(request.getSendAmount(), request.getSendCurrency());

        // Create transaction
        Transaction transaction = Transaction.builder()
            .transactionRef("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
            .sender(sender)
            .receiver(recipient) // Note: This should be a User, but we're using recipient as demonstration
            .sendAmount(request.getSendAmount())
            .sendCurrency(request.getSendCurrency())
            .receiveAmount(receiveAmount)
            .receiveCurrency(recipient.getCurrency())
            .exchangeRate(currencyService.getExchangeRate(request.getSendCurrency(), recipient.getCurrency()).getOurRate())
            .fee(fee)
            .status(Transaction.TransactionStatus.PENDING)
            .type(Transaction.TransactionType.TRANSFER)
            .description(request.getDescription())
            .paymentMethod(paymentMethod)
            .build();

        transaction = transactionRepository.save(transaction);
        log.info("Transaction created with reference: {}", transaction.getTransactionRef());

        return mapToDTO(transaction);
    }

    public TransactionDTO getTransaction(String transactionId, String userId) {
        Transaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        // Verify user has access to this transaction
        if (!transaction.getSender().getId().equals(userId)) {
            throw new GlobalPayException("You do not have access to this transaction");
        }

        return mapToDTO(transaction);
    }

    public Page<TransactionDTO> getUserTransactions(String userId, Pageable pageable) {
        return transactionRepository.findByUserId(userId, pageable)
            .map(this::mapToDTO);
    }

    public Page<TransactionDTO> getSentTransactions(String userId, Pageable pageable) {
        return transactionRepository.findSentTransactions(userId, pageable)
            .map(this::mapToDTO);
    }

    public Page<TransactionDTO> getReceivedTransactions(String userId, Pageable pageable) {
        return transactionRepository.findReceivedTransactions(userId, pageable)
            .map(this::mapToDTO);
    }

    public TransactionDTO updateTransactionStatus(String transactionId, Transaction.TransactionStatus status) {
        Transaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        transaction.setStatus(status);
        if (status == Transaction.TransactionStatus.COMPLETED) {
            transaction.setCompletedAt(LocalDateTime.now());
        }

        transaction = transactionRepository.save(transaction);
        log.info("Transaction {} status updated to {}", transactionId, status);

        return mapToDTO(transaction);
    }

    private TransactionDTO mapToDTO(Transaction transaction) {
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
}
