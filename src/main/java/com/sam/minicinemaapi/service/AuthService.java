package com.sam.minicinemaapi.service;

import com.sam.minicinemaapi.dto.request.AuthenticateRequest;
import com.sam.minicinemaapi.dto.request.UserRegistrationRequest;
import com.sam.minicinemaapi.dto.response.AuthResponse;
import com.sam.minicinemaapi.dto.response.UserResponse;

public interface AuthService {
    AuthResponse login(AuthenticateRequest request);

    UserResponse register(UserRegistrationRequest request);

    AuthResponse refresh(String token);
}
