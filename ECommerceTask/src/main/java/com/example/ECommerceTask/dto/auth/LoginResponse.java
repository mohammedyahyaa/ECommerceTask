package com.example.ECommerceTask.dto.auth;

import com.example.ECommerceTask.domain.Enums.Role;

public class LoginResponse {

    private String token;
    private String username;
    private Role role;

    public LoginResponse(String token, String username, Role role) {
        this.token = token;
        this.username = username;
        this.role = role;
    }

    // Getters & Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

