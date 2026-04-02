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
@Table(name = "refresh_tokens")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshToken extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @Column(name = "token", nullable = false)
    String token;

    @Column(name = "expires_at")
    Instant expiresAt;

    @Column(name = "revoked")
    Boolean revoked;

    public void revoke() {
        this.revoked = true;
    }

    public boolean isExpired() {
        return this.expiresAt.isBefore(Instant.now());
    }
}
