package com.olgo.cookbook.service;

import com.olgo.cookbook.dto.UserDetailDto;
import com.olgo.cookbook.dto.UserDto;
import com.olgo.cookbook.dto.requests.PasswordUpdateDto;
import com.olgo.cookbook.dto.requests.UsernameUpdate;
import com.olgo.cookbook.model.User;
import com.olgo.cookbook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<UserDto> findByUsernameContainingIgnoreCase(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username).stream().map(UserDto::from).toList();
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    public void doIfUserExists(String userId, Consumer<User> action) {
        findById(UUID.fromString(userId)).ifPresent(action);
    }

    public Set<User> getFollowing(User user) {
        return user.getFollowing();
    }

    public UserDetailDto getUserDetail(UUID id) {
        User user = userRepository.findById(id).orElseThrow();
        return new UserDetailDto(user);
    }

    public void updatePassword(UUID id, PasswordUpdateDto passwordUpdateDto) {
        User user = userRepository.findById(id).orElseThrow();
        if (!passwordEncoder.matches(passwordUpdateDto.oldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password does not match");
        }
        user.setPassword(passwordEncoder.encode(passwordUpdateDto.newPassword()));
        userRepository.save(user);
    }

    public void updateUsername(UUID id, UsernameUpdate update) {
        User user = userRepository.findById(id).orElseThrow();
        if (!passwordEncoder.matches(update.password(), user.getPassword())) {
            throw new RuntimeException("Password does not match");
        }
        user.setUsername(update.newUsername());
        userRepository.save(user);
    }

}
