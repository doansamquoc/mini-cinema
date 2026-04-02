package com.sam.minicinemaapi.dto.request;

public record ResetPasswordRequest(String token, String newPassword) {}
