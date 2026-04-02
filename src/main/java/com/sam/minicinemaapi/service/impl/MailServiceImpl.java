package com.sam.minicinemaapi.service.impl;

import com.sam.minicinemaapi.config.AppProperties;
import com.sam.minicinemaapi.dto.request.BulkEmailRequest;
import com.sam.minicinemaapi.dto.request.EmailRequest;
import com.sam.minicinemaapi.exception.BulkEmailException;
import com.sam.minicinemaapi.exception.EmailSendException;
import com.sam.minicinemaapi.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailServiceImpl implements MailService {
    JavaMailSender sender;
    SpringTemplateEngine engine;
    AppProperties properties;

    @Override
    public void sendSimpleEmail(EmailRequest request) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(properties.getEmailFrom());
            message.setTo(request.to());
            message.setSubject(request.subject());
            message.setText(request.body());

            if (request.cc() != null) message.setCc(request.cc());
            if (request.bcc() != null) message.setBcc(request.bcc());

            sender.send(message);
            log.info("Simple mail sent successfully to: {}", request.to());
        } catch (MailException me) {
            log.error("Failed to send email to: {}", request.to());
        }
    }

    @Override
    public void sendHtmlMail(EmailRequest request) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            helper.setFrom(properties.getEmailFrom());
            helper.setTo(request.to());
            helper.setSubject(request.subject());
            helper.setText(request.body(), true);
            helper.setReplyTo(properties.getEmailReplyTo());

            if (request.cc() != null) helper.setCc(request.cc());
            if (request.bcc() != null) helper.setBcc(request.bcc());

            sender.send(message);
            log.info("HTML mail sent successfully to: {}", request.to());
        } catch (MessagingException | MailException me) {
            log.error("Failed to send HTML email", me);
            throw new RuntimeException(me);
        }
    }

    @Override
    public void sendTemplatedMail(String to, String subject, String templateName, Map<String, Object> variables) {
        try {
            Context context = new Context();
            context.setVariables(variables);

            String htmlContent = engine.process(templateName, context);
            EmailRequest request = new EmailRequest(to, subject, htmlContent, null, null, null, true);

            sendHtmlMail(request);
        } catch (MailException e) {
            log.error("Failed to send templated email", e);
        }
    }

    @Override
    public void sendMailWithAttachment(EmailRequest request, byte[] attachment, String attachName) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            helper.setFrom(properties.getEmailFrom());
            helper.setTo(request.to());
            helper.setSubject(request.subject());
            helper.setText(request.body(), request.html());

            helper.addAttachment(attachName, new ByteArrayResource(attachment), getContentType(attachName));

            sender.send(message);
            log.info("Email with attachment sent to: {}", request.to());
        } catch (MessagingException e) {
            log.error("Failed to send email with attachment", e);
            throw new EmailSendException("Failed to send email with attachment " + e);
        }
    }

    @Override
    public void sendBulkEmail(BulkEmailRequest request) {
        List<String> failedRecipients = new ArrayList<>();

        for (String recipient : request.recipients()) {
            try {
                sendTemplatedMail(recipient, request.subject(), request.templateName(), request.templateVariables());
                Thread.sleep(100);
            } catch (Exception e) {
                failedRecipients.add(recipient);
                log.error("Failed to send email to: {}", recipient, e);
            }
        }

        if (!failedRecipients.isEmpty()) throw new BulkEmailException("Failed to send to: " + failedRecipients);
    }

    @Override
    @Async("emailTaskExecutor")
    public CompletableFuture<Void> sendAsyncEmail(EmailRequest emailRequest) {
        try {
            sendHtmlMail(emailRequest);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            log.error("Async email failed", e);
            return CompletableFuture.failedFuture(e);
        }
    }

    private String getContentType(String fileName) {
        if (fileName.endsWith(".pdf")) return MediaType.APPLICATION_PDF_VALUE;
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) return MediaType.IMAGE_JPEG_VALUE;
        if (fileName.endsWith(".png")) return MediaType.IMAGE_PNG_VALUE;
        return MediaType.APPLICATION_OCTET_STREAM_VALUE;
    }
}
