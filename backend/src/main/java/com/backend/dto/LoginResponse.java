package com.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class LoginResponse {
    private String token;
    private String email;
    private String role;
    private String message;
    private boolean success;

    public LoginResponse() {
        this.token = "";
        this.email = "";
        this.role = "";
        this.message = "";
        this.success = false;
    }

    public LoginResponse(String token, String email, String role, String message, boolean success) {
        this.token = token != null ? token : "";
        this.email = email != null ? email : "";
        this.role = role != null ? role : "";
        this.message = message != null ? message : "";
        this.success = success;
    }

    // Explicit getters and setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token != null ? token : "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email != null ? email : "";
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role != null ? role : "";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message != null ? message : "";
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
} 