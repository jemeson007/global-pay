package com.globalpay.service;

import com.globalpay.api.dto.UserDTO;
import com.globalpay.api.request.LoginRequest;
import com.globalpay.api.request.RegisterRequest;
import com.globalpay.api.response.AuthResponse;
import com.globalpay.entity.User;
import com.globalpay.exception.GlobalPayException;
import com.globalpay.exception.ResourceNotFoundException;
import com.globalpay.repository.UserRepository;
import com.globalpay.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new GlobalPayException("Email already registered");
        }

        User user = User.builder()
            .email(request.getEmail())
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .phoneNumber(request.getPhoneNumber())
            .passwordHash(passwordEncoder.encode(request.getPassword()))
            .kycStatus(User.KycStatus.PENDING)
            .emailVerified(false)
            .phoneVerified(false)
            .twoFactorEnabled(false)
            .active(true)
            .defaultCurrency("USD")
            .build();

        user = userRepository.save(user);
        log.info("User registered successfully with id: {}", user.getId());

        return generateAuthResponse(user);
    }

    public AuthResponse login(LoginRequest request) {
        log.info("Authenticating user with email: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new GlobalPayException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new GlobalPayException("Invalid email or password");
        }

        if (!user.getActive()) {
            throw new GlobalPayException("Account is inactive");
        }

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        log.info("User authenticated successfully: {}", user.getId());
        return generateAuthResponse(user);
    }

    public UserDTO getUserProfile(String userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return mapToDTO(user);
    }

    public UserDTO updateUserProfile(String userId, UserDTO updateRequest) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (updateRequest.getFirstName() != null) {
            user.setFirstName(updateRequest.getFirstName());
        }
        if (updateRequest.getLastName() != null) {
            user.setLastName(updateRequest.getLastName());
        }
        if (updateRequest.getDefaultCurrency() != null) {
            user.setDefaultCurrency(updateRequest.getDefaultCurrency());
        }

        user = userRepository.save(user);
        return mapToDTO(user);
    }

    public void changePassword(String userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new GlobalPayException("Current password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Password changed for user: {}", userId);
    }

    private AuthResponse generateAuthResponse(User user) {
        String accessToken = jwtTokenProvider.generateToken(user.getId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        return AuthResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .tokenType("Bearer")
            .expiresIn(86400L) // 24 hours
            .user(AuthResponse.UserInfo.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .build())
            .build();
    }

    private UserDTO mapToDTO(User user) {
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
}
