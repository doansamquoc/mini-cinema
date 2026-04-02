package com.sam.minicinemaapi.event;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PasswordResetEvent extends EmailEvent {
    public PasswordResetEvent(Object source, String to, String fullName, String resetToken, String resetLink) {
        super(
                source,
                to,
                "Password Reset Request",
                Map.of("fullName", fullName, "resetToken", resetToken, "resetLink", resetLink)
        );
    }
}
