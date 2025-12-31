package com.example.ECommerceTask.service;

import com.example.ECommerceTask.dto.auth.LoginRequest;
import com.example.ECommerceTask.dto.auth.LoginResponse;
import com.example.ECommerceTask.dto.user.UserRequest;
import com.example.ECommerceTask.dto.user.UserResponse;

public interface AuthenticationService {

    LoginResponse login(LoginRequest request);

    UserResponse register(UserRequest request);
}

