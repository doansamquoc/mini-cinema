package com.sam.minicinemaapi.service.impl;

import com.sam.minicinemaapi.config.AppProperties;
import com.sam.minicinemaapi.constant.ErrorCode;
import com.sam.minicinemaapi.entity.PasswordResetToken;
import com.sam.minicinemaapi.entity.User;
import com.sam.minicinemaapi.exception.BusinessException;
import com.sam.minicinemaapi.repostiory.PasswordResetTokenRepository;
import com.sam.minicinemaapi.service.PasswordResetTokenService;
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
public class PasswordTokenServiceImpl implements PasswordResetTokenService {
    AppProperties app;
    PasswordResetTokenRepository repository;

    @Override
    public PasswordResetToken createToken(User user) {
        String token = UUIDUtil.generate();
        Instant expiresAt = Instant.now().plusMillis(app.getPasswordResetTokenExpiration());
        PasswordResetToken passwordResetToken = new PasswordResetToken(user, token, expiresAt, false);
        return repository.save(passwordResetToken);
    }

    @Override
    public void revoke(String token) {
        PasswordResetToken passwordResetToken = findByToken(token);
        passwordResetToken.revoke();
        repository.save(passwordResetToken);
    }

    @Override
    public void revoke(User user) {
        PasswordResetToken passwordResetToken = findByUser(user);
        passwordResetToken.revoke();
        repository.save(passwordResetToken);
    }

    @Override
    public void revokeAll(User user) {
        List<PasswordResetToken> passwordResetTokens = findAllByUser(user);
        passwordResetTokens.forEach(PasswordResetToken::revoke);
        repository.saveAll(passwordResetTokens);
    }

    @Override
    public PasswordResetToken findByToken(String token) {
        return repository.findByToken(token).orElseThrow(() -> new BusinessException(ErrorCode.INVALID_TOKEN));
    }

    @Override
    public PasswordResetToken findByUser(User user) {
        return repository.findByUser(user).orElseThrow(() -> new BusinessException(ErrorCode.INVALID_TOKEN));
    }

    @Override
    public List<PasswordResetToken> findAllByUser(User user) {
        return repository.findAllByUser(user);
    }
}
