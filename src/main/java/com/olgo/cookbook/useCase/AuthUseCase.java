package com.olgo.cookbook.useCase;

import com.olgo.cookbook.dto.AuthDto;
import com.olgo.cookbook.model.ClientContext;
import jakarta.servlet.http.Cookie;

public interface AuthUseCase {
    AuthDto login(String email, String password, ClientContext ctx);

    Cookie logout(String tokenName, String token);

    AuthDto refresh(String refreshToken, ClientContext ctx);

    void register(String email, String username, String password);


}
