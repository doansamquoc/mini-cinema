package com.sam.minicinemaapi.listener;

import com.sam.minicinemaapi.constant.AppConstant;
import com.sam.minicinemaapi.event.PasswordResetEvent;
import com.sam.minicinemaapi.event.UserRegistrationEvent;
import com.sam.minicinemaapi.service.MailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailEventListener {
    MailService service;

    @Async
    @EventListener
    @Order(1)
    public void handleUserRegistration(UserRegistrationEvent event) {
        log.info("Sending welcome email to: {}", event.getTo());
        service.sendTemplatedMail(
                event.getTo(),
                event.getSubject(),
                AppConstant.WELCOME_EMAIL_TEMPLATE_NAME,
                event.getVariables()
        );
    }

    @Async
    @EventListener
    @Order(1)
    public void handlePasswordReset(PasswordResetEvent event) {
        log.info("Sending password reset email to: {}", event.getTo());
        service.sendTemplatedMail(
                event.getTo(),
                event.getSubject(),
                AppConstant.RESET_PASSWORD_EMAIL_TEMPLATE_NAME,
                event.getVariables()
        );
    }
}
