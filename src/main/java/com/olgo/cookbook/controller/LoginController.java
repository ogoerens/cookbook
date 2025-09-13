package com.olgo.cookbook.controller;

import com.olgo.cookbook.dto.requests.UserLoginRequest;
import com.olgo.cookbook.dto.responses.UserLoginResponse;
import com.olgo.cookbook.model.User;
import com.olgo.cookbook.service.CookieService;
import com.olgo.cookbook.service.JwtService;
import com.olgo.cookbook.service.UserLoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final UserLoginService userLoginService;
    private final JwtService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);


    @Autowired
    LoginController(UserLoginService userLoginService, JwtService jwtService) {
        this.userLoginService = userLoginService;
        this.jwtService = jwtService;
    }

    @PostMapping("/user")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginRequest request, HttpServletResponse response) {
        try {
            User user = userLoginService.authenticate(request.getEmail(), request.getPassword());
            String token = jwtService.generateToken(user.getId().toString());
            Cookie cookie = CookieService.getSecureCookie("token", token);
            response.addCookie(cookie);

            return ResponseEntity.ok(new UserLoginResponse(token));
        } catch (RuntimeException e) {
            logger.error("Login failed for email {}: {}", request.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
