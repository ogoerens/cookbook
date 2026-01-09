package com.olgo.cookbook.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Service
public class SigningService {

    private static final String HMAC_ALGO = "HmacSHA256";
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private final SecretKey secretKey;

    public SigningService(@Value("${signing.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(CHARSET), HMAC_ALGO);
    }

    public String signUuid(UUID uuid, long expiresAt) {
        String payload = uuid + ":" + expiresAt;
        try {
            Mac mac = Mac.getInstance(HMAC_ALGO);
            mac.init(secretKey);
            byte[] sig = mac.doFinal(payload.getBytes(CHARSET));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(sig);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to sign UUID", e);
        }
    }

    public boolean verify(UUID uuid, long expiresAt, String signature) {
        if (Instant.now().getEpochSecond() > expiresAt) {
            return false;
        }
        String expected = signUuid(uuid, expiresAt);

        return MessageDigest.isEqual(expected.getBytes(CHARSET), signature.getBytes(CHARSET));
    }
}

