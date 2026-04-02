package com.sam.minicinemaapi.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record EmailRequest(
        @NotBlank(message = "email.validation.recipient_required")
        @Email(message = "email.validation.invalid_format")
        String to,

        @NotBlank(message = "email.validation.subject_required")
        String subject,

        @NotBlank(message = "email.validation.body_required")
        String body,

        String cc,
        String bcc,
        List<String> attachments,
        Boolean html
) {}
