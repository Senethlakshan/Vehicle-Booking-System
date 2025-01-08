package com.jxg.isn_backend.dto.auth;

public record SignUpRequestDTO(
        String email,
        String password,
        String firstName,
        String lastName
) {
}
