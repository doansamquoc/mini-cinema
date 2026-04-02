package com.sam.minicinemaapi.service.impl;

import com.sam.minicinemaapi.constant.AuthConstant;
import com.sam.minicinemaapi.constant.ErrorCode;
import com.sam.minicinemaapi.dto.request.AuthenticateRequest;
import com.sam.minicinemaapi.dto.request.UserRegistrationRequest;
import com.sam.minicinemaapi.dto.response.AuthResponse;
import com.sam.minicinemaapi.dto.response.UserResponse;
import com.sam.minicinemaapi.entity.RefreshToken;
import com.sam.minicinemaapi.entity.User;
import com.sam.minicinemaapi.exception.BusinessException;
import com.sam.minicinemaapi.mapper.UserMapper;
import com.sam.minicinemaapi.security.jwt.JwtProvider;
import com.sam.minicinemaapi.security.model.UserPrincipal;
import com.sam.minicinemaapi.service.AuthService;
import com.sam.minicinemaapi.service.RefreshTokenService;
import com.sam.minicinemaapi.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
    UserMapper userMapper;
    JwtProvider jwtProvider;
    UserService userService;
    PasswordEncoder encoder;
    AuthenticationManager manager;
    RefreshTokenService refreshTokenService;

    private UserPrincipal authenticate(String identifier, String password) {
        try {
            Authentication auth = manager.authenticate(new UsernamePasswordAuthenticationToken(identifier, password));
            return (UserPrincipal) auth.getPrincipal();
        } catch (BadCredentialsException bce) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        } catch (DisabledException de) {
            throw new BusinessException(ErrorCode.ACCOUNT_DISABLED);
        } catch (LockedException le) {
            throw new BusinessException(ErrorCode.ACCOUNT_LOCKED);
        }
    }

    @Override
    public AuthResponse login(AuthenticateRequest request) {
        UserPrincipal principal = authenticate(request.identifier(), request.password());
        User user = userService.getReference(principal.getId());

        String accessToken = generateAccessToken(principal);
        RefreshToken refreshToken = refreshTokenService.createToken(user);

        return new AuthResponse(refreshToken.getToken(), accessToken);
    }

    @Override
    public UserResponse register(UserRegistrationRequest request) {
        if (userService.existsByEmail(request.email())) {
            throw new BusinessException(ErrorCode.RESOURCE_CONFLICT, "Email");
        }
        if (userService.existsByPhoneNumber(request.phoneNumber())) {
            throw new BusinessException(ErrorCode.RESOURCE_CONFLICT, "Phone number");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(encoder.encode(request.password()));

        return userMapper.toResponse(userService.createUser(user));
    }

    @Override
    public AuthResponse refresh(String token) {
        RefreshToken oldToken = refreshTokenService.findByToken(token);
        if (oldToken.isExpired()) throw new BusinessException(ErrorCode.INVALID_TOKEN);
        refreshTokenService.revoke(token);

        User user = oldToken.getUser();
        UserPrincipal userPrincipal = UserPrincipal.create(user);

        String newAccessToken = generateAccessToken(userPrincipal);
        RefreshToken newRefreshToken = refreshTokenService.createToken(user);

        return new AuthResponse(newRefreshToken.getToken(), newAccessToken);
    }

    private String generateAccessToken(UserPrincipal principal) {
        List<String> roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .map(role -> role.replace(AuthConstant.ROLE_PREFIX, ""))
                .toList();
        return jwtProvider.generate(principal.getId(), roles);
    }
}
