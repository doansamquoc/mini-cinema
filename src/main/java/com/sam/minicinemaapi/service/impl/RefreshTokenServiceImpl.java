package com.sam.minicinemaapi.service.impl;

import com.sam.minicinemaapi.config.AppProperties;
import com.sam.minicinemaapi.constant.ErrorCode;
import com.sam.minicinemaapi.entity.RefreshToken;
import com.sam.minicinemaapi.entity.User;
import com.sam.minicinemaapi.exception.BusinessException;
import com.sam.minicinemaapi.repostiory.RefreshTokenRepository;
import com.sam.minicinemaapi.service.RefreshTokenService;
import com.sam.minicinemaapi.util.UUIDUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RefreshTokenServiceImpl implements RefreshTokenService {
    RefreshTokenRepository repository;
    AppProperties properties;

    @Override
    public RefreshToken createToken(User user) {
        String token = UUIDUtil.generate();
        Instant expiresAt = Instant.now().plusMillis(properties.getRefreshTokenExpiration());
        RefreshToken refreshToken = new RefreshToken(user, token, expiresAt, false);
        return repository.save(refreshToken);
    }

    @Override
    public void revoke(String token) {
        RefreshToken refreshToken = findByToken(token);
        refreshToken.revoke();
        repository.save(refreshToken);
    }

    @Override
    public void revoke(User user) {
        RefreshToken refreshToken = findByUser(user);
        refreshToken.revoke();
        repository.save(refreshToken);
    }

    @Override
    public void revokeAll(User user) {
        List<RefreshToken> refreshTokens = findAllByUser(user);
        refreshTokens.forEach(RefreshToken::revoke);
        repository.saveAll(refreshTokens);
    }

    @Override
    public RefreshToken findByToken(String token) {
        return repository.findByToken(token).orElseThrow(() -> new BusinessException(ErrorCode.INVALID_TOKEN));
    }

    @Override
    public RefreshToken findByUser(User user) {
        return repository.findByUser(user).orElseThrow(() -> new BusinessException(ErrorCode.INVALID_TOKEN));
    }

    @Override
    public List<RefreshToken> findAllByUser(User user) {
        return repository.findAllByUser(user);
    }
}
