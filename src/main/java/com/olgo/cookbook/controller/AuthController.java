package com.olgo.cookbook.controller;

import com.olgo.cookbook.dto.requests.UserLoginRequest;
import com.olgo.cookbook.dto.responses.UserLoginResponse;
import com.olgo.cookbook.model.User;
import com.olgo.cookbook.service.CookieService;
import com.olgo.cookbook.service.JwtService;
import com.olgo.cookbook.service.UserLoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final CookieService cookieService;
    private final UserLoginService userLoginService;
    private final JwtService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginRequest request, HttpServletResponse response) {
        try {
            User user = userLoginService.authenticate(request.getEmail(), request.getPassword());
            String token = jwtService.generateToken(user.getId().toString());
            Cookie cookie = cookieService.getSecureCookie("token", token);
            response.addCookie(cookie);

            return ResponseEntity.ok(new UserLoginResponse(token));
        } catch (RuntimeException e) {
            logger.error("Login failed for email {}: {}", request.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = cookieService.deleteCookie("token");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status")
    public ResponseEntity<?> authStatus(@AuthenticationPrincipal(expression = "id") UUID userid) {
        if (userid == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.noContent().build();
    }
}
