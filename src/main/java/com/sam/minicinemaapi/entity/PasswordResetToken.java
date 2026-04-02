package com.sam.minicinemaapi.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "password_reset_tokens")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordResetToken extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;
    String token;
    Instant expiresAt;
    Boolean revoked;

    public void revoke() {
        this.revoked = true;
    }

    public boolean revoked() {
        return this.revoked;
    }

    public boolean expired() {
        return this.expiresAt.isBefore(Instant.now());
    }
}
