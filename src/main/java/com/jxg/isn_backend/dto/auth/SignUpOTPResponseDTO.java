package com.jxg.isn_backend.dto.auth;

import java.time.LocalDateTime;

public record SignUpOTPResponseDTO(
        String message,
        String email,
        LocalDateTime expirationTime
) {
}
