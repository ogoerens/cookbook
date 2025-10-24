package com.olgo.cookbook.service;

import com.olgo.cookbook.config.CookieConfig;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CookieService {

    private final CookieConfig cookieConfig;

    public Cookie getSecureCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(cookieConfig.isSecure());
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 12);

        return cookie;
    }

    public Cookie deleteCookie(String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(cookieConfig.isSecure());
        cookie.setPath("/");
        cookie.setMaxAge(0);

        return cookie;
    }
}
