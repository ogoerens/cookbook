package com.olgo.cookbook.controller;

import com.olgo.cookbook.dto.requests.UserRegisterRequest;
import com.olgo.cookbook.model.User;
import com.olgo.cookbook.service.UserRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    private final UserRegistrationService registrationService;
    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);


    @Autowired
    public RegistrationController(UserRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/user")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequest request) {
        try {
            logger.info("Registering user with email: {}", request.getEmail());
            User user = registrationService.registerUser(request.getEmail(), LocalDate.now(), request.getUsername(), request.getPassword());
            return ResponseEntity.ok(Map.of("message", "User registered successfully", "userId", user.getId()));
        } catch (RuntimeException e) {
            logger.error("Registration failed for email {}: {}", request.getEmail(), e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
