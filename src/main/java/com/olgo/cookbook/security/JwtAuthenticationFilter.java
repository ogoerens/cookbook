package com.olgo.cookbook.security;

import com.olgo.cookbook.model.User;
import com.olgo.cookbook.service.JwtService;
import com.olgo.cookbook.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    public JwtAuthenticationFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7); // strip "Bearer "
        final String userId = jwtService.extractUserId(jwt);

        if (userId == null && authenticationExists()) {
            filterChain.doFilter(request, response);
            return;
        }

        userService.doIfUserExists(userId, user -> {
            if (jwtService.isTokenValid(jwt, user.getId().toString())) {
                addAuthenticationInSecurityContext(user, request);
            }
        });

        filterChain.doFilter(request, response);

    }

    private boolean authenticationExists() {
        return SecurityContextHolder.getContext().getAuthentication() != null;
    }

    private void addAuthenticationInSecurityContext(User user, HttpServletRequest request) {
        var authToken = createTokenForAuthenticatedUser(user, request);
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private UsernamePasswordAuthenticationToken createTokenForAuthenticatedUser(User user, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user, null, null
        );
        authToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );
        return authToken;
    }
}
