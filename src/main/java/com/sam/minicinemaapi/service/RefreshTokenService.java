package com.sam.minicinemaapi.service;

import com.sam.minicinemaapi.entity.RefreshToken;
import com.sam.minicinemaapi.entity.User;

import java.util.List;

public interface RefreshTokenService {
    RefreshToken createToken(User user);

    void revoke(String token);

    void revoke(User user);

    void revokeAll(User user);

    RefreshToken findByToken(String token);

    RefreshToken findByUser(User user);

    List<RefreshToken> findAllByUser(User user);
}
