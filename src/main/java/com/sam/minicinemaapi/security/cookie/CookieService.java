package com.sam.minicinemaapi.security.cookie;

import com.sam.minicinemaapi.config.AppProperties;
import com.sam.minicinemaapi.constant.AppConstant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CookieService {
    AppProperties app;

    public ResponseCookie createCookie(String name, String value, long maxAge, String path) {
        return ResponseCookie
                .from(name, value)
                .httpOnly(true)
                .secure(app.isProduction())
                .sameSite("Lax")
                .maxAge(maxAge)
                .path(path)
                .build();
    }

    public ResponseCookie createRefreshCookie(String value) {
        long maxAgeInSeconds = app.getRefreshTokenExpiration() / 1000;
        return createCookie(AppConstant.REFRESH_TOKEN_NAME, value, maxAgeInSeconds, AppConstant.REFRESH_TOKEN_PATH);
    }
}
