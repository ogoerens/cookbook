package com.olgo.cookbook.controller;

import com.olgo.cookbook.dto.UserDto;
import com.olgo.cookbook.dto.requests.UserRegisterRequest;
import com.olgo.cookbook.model.User;
import com.olgo.cookbook.service.UserRegistrationService;
import com.olgo.cookbook.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserRegistrationService registrationService;

    @PostMapping("/register")
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

    @GetMapping()
    public ResponseEntity<?> findUsers(@RequestParam String q) {
        List<UserDto> users = userService.findByUsernameContainingIgnoreCase(q);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/following")
    public ResponseEntity<Set<UserDto>> getFollowing(@AuthenticationPrincipal(expression = "id") UUID userId) {
        User user = userService.findById(userId).orElseThrow();
        Set<UserDto> following = userService.getFollowing(user).stream().map(UserDto::from).collect(Collectors.toSet());
        return ResponseEntity.ok(following);
    }

    @PutMapping("/following/{id}")
    public ResponseEntity<?> follow(@AuthenticationPrincipal(expression = "id") UUID userId, @PathVariable UUID id) {
        User user = userService.findById(userId).orElseThrow();
        User following = userService.findById(id).orElseThrow();
        user.follow(following);
        userService.save(user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/following/{id}")
    public ResponseEntity<?> unfollow(@AuthenticationPrincipal(expression = "id") UUID userId, @PathVariable UUID id) {
        User follower = userService.findById(userId).orElseThrow();
        User followed = userService.findById(id).orElseThrow();
        follower.unfollow(followed);
        userService.save(follower);
        return ResponseEntity.noContent().build();
    }


}
