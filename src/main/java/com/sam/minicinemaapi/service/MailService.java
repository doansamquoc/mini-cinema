package com.sam.minicinemaapi.service;

import com.sam.minicinemaapi.dto.request.BulkEmailRequest;
import com.sam.minicinemaapi.dto.request.EmailRequest;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface MailService {
    void sendSimpleEmail(EmailRequest request);

    void sendHtmlMail(EmailRequest request);

    void sendTemplatedMail(String to, String subject, String templateName, Map<String, Object> variables);

    void sendMailWithAttachment(EmailRequest request, byte[] attachment, String attachName);

    void sendBulkEmail(BulkEmailRequest request);

    CompletableFuture<Void> sendAsyncEmail(EmailRequest emailRequest);
}
