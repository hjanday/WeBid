package com.webid.webid.dto;

public class UpdatePasswordRequestDTO {
    private String email;
    private String newPassword;

    public String getEmail() {
        return email;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
