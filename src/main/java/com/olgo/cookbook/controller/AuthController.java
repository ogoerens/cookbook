package com.olgo.cookbook.controller;

import com.olgo.cookbook.dto.AuthDto;
import com.olgo.cookbook.dto.requests.UserLoginRequest;
import com.olgo.cookbook.dto.requests.UserRegisterRequest;
import com.olgo.cookbook.dto.responses.UserLoginResponse;
import com.olgo.cookbook.model.ClientContext;
import com.olgo.cookbook.service.CookieService;
import com.olgo.cookbook.useCase.AuthUseCase;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.olgo.cookbook.utils.RequestUtils.getClientContext;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthUseCase auth;
    private final CookieService cookieService;

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> loginUser(@RequestBody UserLoginRequest body, HttpServletRequest request, HttpServletResponse response) {
        ClientContext ctx = getClientContext(request);

        AuthDto authDto = auth.login(body.getEmail(), body.getPassword(), ctx);
        Cookie cookie = cookieService.getSecureCookie("token", authDto.refreshToken());
        response.addCookie(cookie);

        return ResponseEntity.ok(new UserLoginResponse(authDto.accessToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(name = "token", required = false) String refreshToken, HttpServletResponse response) {
        Cookie cookie = auth.logout("token", refreshToken);
        response.addCookie(cookie);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequest request) {
        auth.register(request.getEmail(), request.getUsername(), request.getPassword());
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<UserLoginResponse> refresh(@CookieValue(name = "token", required = false) String refreshToken, HttpServletRequest request, HttpServletResponse response) {
        ClientContext clientContext = getClientContext(request);
        AuthDto authDto = auth.refresh(refreshToken, clientContext);
        Cookie cookie = cookieService.getSecureCookie("token", authDto.refreshToken());
        response.addCookie(cookie);

        return ResponseEntity.ok(new UserLoginResponse(authDto.accessToken()));
    }

    @GetMapping("/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void authStatus() {
    }
}
