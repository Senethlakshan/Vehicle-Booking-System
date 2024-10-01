package com.jxg.isn_backend.dto.auth;

public record PasswordResetRequestDTO (String token, String newPassword) {

}