package com.olgo.cookbook.service;

import com.olgo.cookbook.exceptions.UnauthorizedException;
import com.olgo.cookbook.model.ClientContext;
import com.olgo.cookbook.model.RefreshToken;
import com.olgo.cookbook.model.User;
import com.olgo.cookbook.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

import static com.olgo.cookbook.utils.HashUtils.sha256Base64;

@RequiredArgsConstructor
@Service
public class TokenService {

    private static final SecureRandom RNG = new SecureRandom();
    private static final Duration REFRESH_TTL_D = Duration.ofDays(14);

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Issues a new raw refresh token for a user and stores its hash in DB.
     */
    @Transactional
    public String issueRefreshTokenForUser(User user, ClientContext ctx) {

        String rawToken = generateOpaqueToken();
        String tokenHash = sha256Base64(rawToken);

        Instant now = Instant.now();
        Instant expiresAt = now.plus(REFRESH_TTL_D);

        RefreshToken entity = new RefreshToken(
                tokenHash,
                user,
                now,
                expiresAt,
                ctx.userAgent(),
                ctx.ipAddr()
        );

        refreshTokenRepository.save(entity);
        return rawToken;
    }

    /**
     * Validates a refresh token from cookie and returns the user.
     */
    @Transactional(readOnly = true)
    public User validateAndGetUser(String rawRefreshToken) {
        String tokenHash = sha256Base64(rawRefreshToken);

        RefreshToken stored = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        Instant now = Instant.now();
        if (stored.isRevoked()) {
            throw new UnauthorizedException("Refresh token revoked");
        }
        if (stored.isExpired(now)) {
            throw new UnauthorizedException("Refresh token expired");
        }

        return stored.getUser();
    }

    @Transactional
    public String rotateRefreshToken(String oldRawRefreshToken, ClientContext ctx) {
        String oldHash = sha256Base64(oldRawRefreshToken);
        Instant now = Instant.now();

        RefreshToken storedToken = refreshTokenRepository.findByTokenHash(oldHash)
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        if (storedToken.isRevoked() || storedToken.isExpired(now)) {
            throw new UnauthorizedException("Refresh token not usable");
        }

        storedToken.revoke(now);
        refreshTokenRepository.save(storedToken);

        String newRefreshTokenRaw = generateOpaqueToken();
        String newHash = sha256Base64(newRefreshTokenRaw);
        Instant expiresAt = now.plus(REFRESH_TTL_D);

        RefreshToken refreshToken = new RefreshToken(newHash, storedToken.getUser(), now, expiresAt, ctx.userAgent(), ctx.ipAddr());

        refreshTokenRepository.save(refreshToken);
        return newRefreshTokenRaw;
    }

    @Transactional
    public void revoke(String rawRefreshToken) {
        if (rawRefreshToken == null || rawRefreshToken.isEmpty()) {
            return;
        }

        String hash = sha256Base64(rawRefreshToken);

        refreshTokenRepository.findByTokenHash(hash).ifPresent(token -> {
            if (!token.isRevoked()) {
                token.revoke(Instant.now());
                refreshTokenRepository.save(token);
            }
        });
    }

    private String generateOpaqueToken() {
        byte[] bytes = new byte[64];
        RNG.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
