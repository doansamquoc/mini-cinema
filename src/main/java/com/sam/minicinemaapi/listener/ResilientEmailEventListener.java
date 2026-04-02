package com.sam.minicinemaapi.listener;

import com.sam.minicinemaapi.constant.AppConstant;
import com.sam.minicinemaapi.event.UserRegistrationEvent;
import com.sam.minicinemaapi.exception.MailException;
import com.sam.minicinemaapi.service.MailService;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResilientEmailEventListener {
    MailService service;

    @Async
    @EventListener
    @Retryable(
            retryFor = {MailException.class, MessagingException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void handleWithRetry(UserRegistrationEvent event) {
        try {
            service.sendTemplatedMail(
                    event.getTo(),
                    event.getSubject(),
                    AppConstant.WELCOME_EMAIL_TEMPLATE_NAME,
                    event.getVariables()
            );
        } catch (Exception e) {
            log.error("Failed to send email after retries", e);
        }
    }
}
