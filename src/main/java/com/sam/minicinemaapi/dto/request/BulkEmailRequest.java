package com.sam.minicinemaapi.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Map;

public record BulkEmailRequest(
        @NotEmpty(message = "email.validation.recipients_required")
        List<@Email String> recipients,
        @NotBlank
        String subject,
        String templateName,
        Map<String, Object> templateVariables
) {}
