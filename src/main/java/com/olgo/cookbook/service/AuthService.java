package com.olgo.cookbook.service;

import com.olgo.cookbook.model.User;
import com.olgo.cookbook.useCase.AuthUseCase;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

    private final CookieService cookieService;
    private final UserLoginService userLoginService;
    private final JwtService jwtService;
    private final UserRegistrationService userRegistrationService;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);


    @Override
    public Cookie login(String email, String password) {
        logger.info("Logging in user with email: {}", email);

        User user = userLoginService.authenticate(email, password);
        String token = jwtService.generateToken(user.getId().toString());
        return cookieService.getSecureCookie("token", token);
    }

    @Override
    public Cookie logout(String token) {
        return cookieService.deleteCookie(token);
    }

    @Override
    public void register(String email, String username, String password) {
        logger.info("Registering user with email: {}", email);
        userRegistrationService.registerUser(email, LocalDate.now(), username, password);
    }
}
