package com.olgo.cookbook.security;

import com.olgo.cookbook.model.User;
import com.olgo.cookbook.model.UserPrincipal;
import com.olgo.cookbook.service.JwtService;
import com.olgo.cookbook.service.UserService;
import com.olgo.cookbook.utils.RequestUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

        final String jwt = RequestUtils.extractJwt(request);

        // If still null, skip
        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        final String userId = jwtService.extractUserId(jwt);

        if (userId == null || authenticationExists()) {
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
        UserPrincipal userPrincipal = new UserPrincipal(user.getId(), user.getUsername());
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, null
        );
        authToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );
        return authToken;
    }
}
