package com.jxg.isn_backend.dto.auth;

public class ChangePasswordRequestDTO {
    private String newPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
