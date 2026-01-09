package com.olgo.cookbook.utils;

import com.olgo.cookbook.model.ClientContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

public final class RequestUtils {

    private RequestUtils() {
    }

    public static ClientContext getClientContext(HttpServletRequest request) {
        return new ClientContext(safeHeader(request, HttpHeaders.USER_AGENT), extractClientIp(request));
    }

    private static String extractClientIp(HttpServletRequest request) {
        // Behind reverse proxy --> use X-Forwarded-For
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            String first = xff.split(",")[0].trim();
            return first.length() <= 64 ? first : null;
        }
        String ip = request.getRemoteAddr();
        return (ip != null && ip.length() <= 64) ? ip : null;
    }

    public static String extractJwt(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

    private static String safeHeader(HttpServletRequest req, String name) {
        String v = req.getHeader(name);
        return (v != null && v.length() <= 255) ? v : null;
    }


}
