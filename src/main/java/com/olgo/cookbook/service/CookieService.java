package com.olgo.cookbook.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
    public static Cookie getSecureCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); //TODO:CHANGE to ture
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 12);

        return cookie;
    }

    public static Cookie deleteCookie(String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);//TODO:CHANGE to ture
        cookie.setPath("/");
        cookie.setMaxAge(0);

        return cookie;
    }
}
