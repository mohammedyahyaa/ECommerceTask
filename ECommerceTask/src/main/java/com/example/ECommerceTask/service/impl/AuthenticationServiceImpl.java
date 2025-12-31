package com.example.ECommerceTask.service.impl;

import com.example.ECommerceTask.domain.Entity.User;
import com.example.ECommerceTask.dto.auth.LoginRequest;
import com.example.ECommerceTask.dto.auth.LoginResponse;
import com.example.ECommerceTask.dto.user.UserRequest;
import com.example.ECommerceTask.dto.user.UserResponse;
import com.example.ECommerceTask.repository.UserRepository;
import com.example.ECommerceTask.service.AuthenticationService;
import com.example.ECommerceTask.service.JwtTokenService;
import com.example.ECommerceTask.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserService userService;

    public AuthenticationServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtTokenService jwtTokenService,
            UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.userService = userService;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = jwtTokenService.generateToken(user.getUsername(), user.getRole().name());

        return new LoginResponse(token, user.getUsername(), user.getRole());
    }

    @Override
    public UserResponse register(UserRequest request) {
        return userService.create(request);
    }
}

