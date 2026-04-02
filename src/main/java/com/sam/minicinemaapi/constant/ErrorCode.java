package com.sam.minicinemaapi.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "auth.error.unauthorized"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "auth.error.access_denied"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "auth.error.invalid_credentials"),
    ACCOUNT_DISABLED(HttpStatus.UNAUTHORIZED, "auth.error.account_disabled"),
    ACCOUNT_LOCKED(HttpStatus.UNAUTHORIZED, "auth.error.account_locked"),
    ACCOUNT_EXPIRED(HttpStatus.UNAUTHORIZED, "auth.error.account_expired"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "auth.error.token_expired"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "auth.error.invalid_token"),

    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "error.resource_not_found"),
    RESOURCE_CONFLICT(HttpStatus.CONFLICT, "error.resource_conflict");

    HttpStatus status;
    String messageKey;
}
