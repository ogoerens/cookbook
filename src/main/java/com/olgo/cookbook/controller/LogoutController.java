package com.olgo.cookbook.controller;

import com.olgo.cookbook.service.CookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logout")
@RequiredArgsConstructor
public class LogoutController {

    private final CookieService cookieService;


    @PostMapping("/user")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = cookieService.deleteCookie("token");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
