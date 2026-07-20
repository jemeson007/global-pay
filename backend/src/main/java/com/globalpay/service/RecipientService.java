package com.globalpay.service;

import com.globalpay.api.dto.RecipientDTO;
import com.globalpay.entity.Recipient;
import com.globalpay.entity.User;
import com.globalpay.exception.ResourceNotFoundException;
import com.globalpay.repository.RecipientRepository;
import com.globalpay.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class RecipientService {

    private final RecipientRepository recipientRepository;
    private final UserRepository userRepository;

    public RecipientService(RecipientRepository recipientRepository, UserRepository userRepository) {
        this.recipientRepository = recipientRepository;
        this.userRepository = userRepository;
    }

    public RecipientDTO addRecipient(String userId, RecipientDTO request) {
        log.info("Adding recipient for user: {}", userId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Recipient recipient = Recipient.builder()
            .user(user)
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .phoneNumber(request.getPhoneNumber())
            .currency(request.getCurrency())
            .bankName(request.getBankName())
            .accountHolderName(request.getAccountHolderName())
            .country(request.getCountry())
            .city(request.getCity())
            .address(request.getAddress())
            .isActive(true)
            .build();

        recipient = recipientRepository.save(recipient);
        log.info("Recipient added: {}", recipient.getId());

        return mapToDTO(recipient);
    }

    public List<RecipientDTO> getUserRecipients(String userId) {
        return recipientRepository.findByUserId(userId).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public RecipientDTO getRecipient(String recipientId, String userId) {
        Recipient recipient = recipientRepository.findByIdAndUserId(recipientId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Recipient not found"));
        return mapToDTO(recipient);
    }

    public void deleteRecipient(String recipientId, String userId) {
        Recipient recipient = recipientRepository.findByIdAndUserId(recipientId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Recipient not found"));
        recipientRepository.delete(recipient);
        log.info("Recipient deleted: {}", recipientId);
    }

    public RecipientDTO updateRecipientLastUsed(String recipientId) {
        Recipient recipient = recipientRepository.findById(recipientId)
            .orElseThrow(() -> new ResourceNotFoundException("Recipient not found"));
        recipient.setLastUsedAt(LocalDateTime.now());
        recipient = recipientRepository.save(recipient);
        return mapToDTO(recipient);
    }

    private RecipientDTO mapToDTO(Recipient recipient) {
        return RecipientDTO.builder()
            .id(recipient.getId())
            .firstName(recipient.getFirstName())
            .lastName(recipient.getLastName())
            .email(recipient.getEmail())
            .phoneNumber(recipient.getPhoneNumber())
            .currency(recipient.getCurrency())
            .bankName(recipient.getBankName())
            .accountHolderName(recipient.getAccountHolderName())
            .swiftCode(recipient.getSwiftCode())
            .country(recipient.getCountry())
            .city(recipient.getCity())
            .address(recipient.getAddress())
            .isActive(recipient.getIsActive())
            .createdAt(recipient.getCreatedAt())
            .lastUsedAt(recipient.getLastUsedAt())
            .build();
    }
}
