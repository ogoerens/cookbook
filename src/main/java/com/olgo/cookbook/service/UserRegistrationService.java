package com.olgo.cookbook.service;

import com.olgo.cookbook.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserRegistrationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserRegistrationService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String email, LocalDate joined, String username, String rawPassword) {
        if (email == null || email.isEmpty() || username == null || username.isEmpty() || rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Missing information");
        }
        if (userService.emailExists(email)) {
            throw new RuntimeException("Email is already taken.");
        }
        if (userService.usernameExists(username)) {
            throw new RuntimeException("Username is already taken.");
        }

        String hashedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(email, joined, username, hashedPassword);
        return userService.save(user);
    }
}
