package com.globalpay.api.controller;

import com.globalpay.api.dto.UserDTO;
import com.globalpay.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Slf4j
@Tag(name = "Users", description = "User profile management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getProfile(Authentication authentication) {
        String userId = authentication.getName();
        UserDTO userDTO = userService.getUserProfile(userId);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserDTO> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UserDTO updateRequest) {
        String userId = authentication.getName();
        UserDTO userDTO = userService.updateUserProfile(userId, updateRequest);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            Authentication authentication,
            @RequestBody ChangePasswordRequest request) {
        String userId = authentication.getName();
        userService.changePassword(userId, request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.ok("Password changed successfully");
    }

    public static class ChangePasswordRequest {
        public String oldPassword;
        public String newPassword;
    }
}
