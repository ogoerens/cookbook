package com.olgo.cookbook.controller;

import com.olgo.cookbook.dto.requests.UserLoginRequest;
import com.olgo.cookbook.dto.responses.UserLoginResponse;
import com.olgo.cookbook.model.User;
import com.olgo.cookbook.service.JwtService;
import com.olgo.cookbook.service.UserLoginService;
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

    @Autowired
    LoginController(UserLoginService userLoginService, JwtService jwtService) {
        this.userLoginService = userLoginService;
        this.jwtService = jwtService;
    }

    @PostMapping("/user")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginRequest request) {
        try {
            User user = userLoginService.authenticate(request.getEmail(), request.getPassword());
            String token = jwtService.generateToken(user.getId().toString());
            return ResponseEntity.ok(new UserLoginResponse(token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
