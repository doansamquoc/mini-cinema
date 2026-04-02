package com.sam.minicinemaapi.service;

import com.sam.minicinemaapi.dto.request.UserRegistrationRequest;
import com.sam.minicinemaapi.entity.User;

public interface UserService {
    User findByIdentifier(String identifier);

    User getReference(Long userId);

    User createUser(User user);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);
}
