package com.sam.minicinemaapi.exception;

import com.sam.minicinemaapi.constant.ErrorCode;
import com.sam.minicinemaapi.dto.common.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GlobalExceptionHandler {
    MessageSource messageSource;

    @ExceptionHandler(BusinessException.class)
    ResponseEntity<ErrorResponse> handleBusiness(BusinessException be, HttpServletRequest servletRequest, Locale locale) {
        String translatedMessage = messageSource.getMessage(be.getErrorCode().getMessageKey(), be.getArgs(), locale);
        ErrorResponse response = ErrorResponse.of(
                be.getErrorCode(),
                translatedMessage,
                servletRequest.getRequestURI(),
                servletRequest.getMethod()
        );
        return ResponseEntity.status(be.getErrorCode().getStatus()).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<ErrorResponse> handleAuthentication(
            AuthenticationException ex,
            HttpServletRequest request,
            Locale locale
    ) {

        String message = messageSource.getMessage(
                "auth.error.invalid_credentials",
                null,
                locale
        );

        ErrorResponse response = ErrorResponse.of(
                ErrorCode.INVALID_CREDENTIALS,
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> handleGeneric(Exception e, HttpServletRequest servletRequest, Locale locale) {
        String translatedMessage = messageSource.getMessage("system.error.unknown", null, locale);
        ErrorResponse response = ErrorResponse.of(
                ErrorCode.UNKNOWN,
                translatedMessage,
                e.getMessage(),
                servletRequest.getRequestURI(),
                servletRequest.getMethod()
        );

        log.error("Exception type: {}", e.getClass());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(response);
    }
}
