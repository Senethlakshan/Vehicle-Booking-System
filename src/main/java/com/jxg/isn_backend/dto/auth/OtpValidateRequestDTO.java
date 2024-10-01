package com.jxg.isn_backend.dto.auth;

public record OtpValidateRequestDTO(
        String email,
        String otp
) {
}
