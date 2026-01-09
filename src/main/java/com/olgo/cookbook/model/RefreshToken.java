package com.olgo.cookbook.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;


@Getter
@Setter
@Entity
@Table(
        name = "refresh_tokens",
        indexes = {
                @Index(name = "idx_refresh_token_hash", columnList = "tokenHash", unique = true),
                @Index(name = "idx_refresh_token_user", columnList = "user_id")
        }
)
public class RefreshToken {
    @Id
    @GeneratedValue
    UUID uuid;
    @Column(nullable = false, unique = true, length = 88) // base64 sha256 ~44 chars
    private String tokenHash;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean revoked = false;

    @Column(nullable = true)
    private Instant revokedAt;

    @Column(nullable = true, length = 255)
    private String userAgent;

    @Column(nullable = true, length = 64)
    private String ipAddress;

    public RefreshToken() {
    }

    public RefreshToken(String tokenHash, User user, Instant createdAt, Instant expiresAt, String userAgent, String ipAddress) {
        this.tokenHash = tokenHash;
        this.user = user;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.userAgent = userAgent;
        this.ipAddress = ipAddress;
    }

    public boolean isExpired(Instant now) {
        return expiresAt.isBefore(now) || expiresAt.equals(now);
    }

    public void revoke(Instant now) {
        this.revoked = true;
        this.revokedAt = now;
    }
}
