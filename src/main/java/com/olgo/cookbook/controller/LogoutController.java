package com.olgo.cookbook.controller;

import com.olgo.cookbook.service.CookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logout")
public class LogoutController {

    @PostMapping("/user")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = CookieService.deleteCookie("token");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
