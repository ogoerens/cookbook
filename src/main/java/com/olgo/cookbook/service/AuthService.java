package com.olgo.cookbook.service;

import com.olgo.cookbook.model.User;
import com.olgo.cookbook.useCase.AuthUseCase;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

    private final CookieService cookieService;
    private final UserLoginService userLoginService;
    private final JwtService jwtService;

    @Override
    public Cookie login(String email, String password) {
        User user = userLoginService.authenticate(email, password);
        String token = jwtService.generateToken(user.getId().toString());
        return cookieService.getSecureCookie("token", token);
    }

    @Override
    public Cookie logout(String token) {
        return cookieService.deleteCookie(token);
    }
}
