package com.sam.minicinemaapi.controller;

import com.sam.minicinemaapi.constant.EndpointConstant;
import com.sam.minicinemaapi.dto.common.SuccessResponse;
import com.sam.minicinemaapi.dto.request.AuthenticateRequest;
import com.sam.minicinemaapi.dto.request.UserRegistrationRequest;
import com.sam.minicinemaapi.dto.response.AuthResponse;
import com.sam.minicinemaapi.dto.response.UserResponse;
import com.sam.minicinemaapi.security.cookie.CookieService;
import com.sam.minicinemaapi.service.AuthService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(EndpointConstant.PREFIX_V1 + "/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;
    CookieService cookieService;

    @PostMapping("/login")
    ResponseEntity<SuccessResponse<String>> login(@RequestBody AuthenticateRequest request) {
        AuthResponse response = authService.login(request);
        String cookie = cookieService.createRefreshCookie(response.refreshToken()).toString();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie).body(SuccessResponse.ofData(response.accessToken()));
    }

    @PostMapping("/register")
    ResponseEntity<SuccessResponse<UserResponse>> register(@Valid @RequestBody UserRegistrationRequest request) {
        UserResponse userResponse = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.ofData(userResponse));
    }

    @GetMapping("/refresh")
    ResponseEntity<SuccessResponse<String>> refresh(@CookieValue(name = "refresh-token", required = false) String token) {
        AuthResponse response = authService.refresh(token);
        String cookie = cookieService.createRefreshCookie(response.refreshToken()).toString();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie).body(SuccessResponse.ofData(response.accessToken()));
    }
}
