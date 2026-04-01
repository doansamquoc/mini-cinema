package com.sam.minicinemaapi.security.jwt;

import com.sam.minicinemaapi.constant.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtEntryPointHandler implements AuthenticationEntryPoint {
    AuthResponder responder;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException ae
    ) throws IOException {
        log.error("Unauthorized: {}", ae.getMessage());
        responder.sendError(response, ErrorCode.UNAUTHORIZED, request.getRequestURI());
    }
}
