package com.olgo.cookbook.controller;

import com.olgo.cookbook.dto.requests.UserRegisterRequest;
import com.olgo.cookbook.model.User;
import com.olgo.cookbook.service.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    private final UserRegistrationService registrationService;

    @Autowired
    public RegistrationController(UserRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/user")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequest request) {
        try {
            User user = registrationService.registerUser(request.getEmail(), LocalDate.now(), request.getUsername(), request.getPassword());
            return ResponseEntity.ok("User registered successfully with ID: " + user.getId());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
