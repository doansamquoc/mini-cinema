package com.sam.minicinemaapi.security.jwt;

import com.sam.minicinemaapi.constant.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    AuthResponder responder;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException ade
    ) throws IOException {
        log.error("Access denied: {}", ade.getMessage());
        responder.sendError(response, ErrorCode.ACCESS_DENIED, request.getRequestURI());
    }
}
