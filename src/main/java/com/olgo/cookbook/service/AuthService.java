package com.olgo.cookbook.service;

import com.olgo.cookbook.dto.AuthDto;
import com.olgo.cookbook.model.ClientContext;
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
    private final TokenService tokenService;
    private final UserRegistrationService userRegistrationService;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);


    @Override
    public AuthDto login(String email, String password, ClientContext ctx) {
        logger.info("Logging in user with email: {}", email);

        User user = userLoginService.authenticate(email, password);
        String accessToken = jwtService.generateJWToken(user.getId().toString());
        String refreshToken = tokenService.issueRefreshTokenForUser(user, ctx);
        return new AuthDto(accessToken, refreshToken);
    }

    @Override
    public AuthDto refresh(String refreshToken, ClientContext ctx) {
        User user = tokenService.validateAndGetUser(refreshToken);
        String accessToken = jwtService.generateJWToken(user.getId().toString());
        String rotatedRefreshToken = tokenService.rotateRefreshToken(refreshToken, ctx);

        return new AuthDto(accessToken, rotatedRefreshToken);
    }

    @Override
    public Cookie logout(String tokenName, String token) {
        tokenService.revoke(token);
        return cookieService.deleteCookie(tokenName);
    }

    @Override
    public void register(String email, String username, String password) {
        logger.info("Registering user with email: {}", email);
        userRegistrationService.registerUser(email, LocalDate.now(), username, password);
    }
}
