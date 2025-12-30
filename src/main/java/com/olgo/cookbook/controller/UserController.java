package com.olgo.cookbook.controller;

import com.olgo.cookbook.dto.UserDetailDto;
import com.olgo.cookbook.dto.UserDto;
import com.olgo.cookbook.dto.requests.PasswordUpdateDto;
import com.olgo.cookbook.dto.requests.UsernameUpdate;
import com.olgo.cookbook.model.User;
import com.olgo.cookbook.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDetailDto> me(@AuthenticationPrincipal(expression = "id") UUID userId) {
        UserDetailDto userDetailDto = userService.getUserDetail(userId);
        return ResponseEntity.ok(userDetailDto);
    }

    @PatchMapping("/me/password")
    public ResponseEntity<?> updatePassword(@AuthenticationPrincipal(expression = "id") UUID userId,
                                            @Valid @RequestBody() PasswordUpdateDto passwordUpdateDto) {
        userService.updatePassword(userId, passwordUpdateDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/me/username")
    public ResponseEntity<UserDetailDto> updateUsername(@AuthenticationPrincipal(expression = "id") UUID userId, @RequestBody UsernameUpdate usernameUpdate) {
        userService.updateUsername(userId, usernameUpdate);
        return ResponseEntity.ok().build();
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
