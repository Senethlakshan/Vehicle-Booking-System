package com.jxg.isn_backend.dto.auth;

public record SignInRequestDTO(
        String email,
        String password
) {
}
