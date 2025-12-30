package com.olgo.cookbook.useCase;

import jakarta.servlet.http.Cookie;

public interface AuthUseCase {
    Cookie login(String email, String password);

    Cookie logout(String token);

    void register(String email, String username, String password);


}
