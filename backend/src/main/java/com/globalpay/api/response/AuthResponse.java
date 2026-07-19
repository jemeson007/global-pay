package com.globalpay.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
    private UserInfo user;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserInfo {
        private String id;
        private String email;
        private String firstName;
        private String lastName;
        private String phoneNumber;
    }
}
