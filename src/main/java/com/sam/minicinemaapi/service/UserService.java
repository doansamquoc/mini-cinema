package com.sam.minicinemaapi.service;

import com.sam.minicinemaapi.entity.User;

import java.util.Optional;

public interface UserService {
    void updatePassword(User user, String newPassword);

    User findByIdentifier(String identifier);

    User findByEmail(String email);

    Optional<User> findOptionByEmail(String email);

    User getReference(Long userId);

    User createUser(User user);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);
}
