package com.sam.minicinemaapi.service;

import com.sam.minicinemaapi.entity.PasswordResetToken;
import com.sam.minicinemaapi.entity.User;

import java.util.List;

public interface PasswordResetTokenService {
    PasswordResetToken createToken(User user);

    void revoke(String token);

    void revoke(User user);

    void revokeAll(User user);

    PasswordResetToken findByToken(String token);

    PasswordResetToken findByUser(User user);

    List<PasswordResetToken> findAllByUser(User user);
}
