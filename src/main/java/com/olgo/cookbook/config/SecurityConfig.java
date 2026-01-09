package com.olgo.cookbook.config;

import com.olgo.cookbook.security.JwtAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        logger.info("FilterChain started");
        http
                .csrf(csrf -> csrf.disable())
                .cors(c -> c.configurationSource(corsConfigurationSource))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Preflight should always be allowed
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Open auth/registration endpoints
                        .requestMatchers("/api/auth/register", "/api/auth/login", "api/auth/refresh", "api/auth/logout").permitAll()

                        //Images with signedUrls
                        .requestMatchers(HttpMethod.GET, "/api/pictures/*/signed-url").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/pictures/*").permitAll()

                        // (optional) health/open endpoints
                        .requestMatchers("/api/actuator/health").permitAll()

                        // everything else requires auth
                        .anyRequest().authenticated()
                )
                // We use JWT, not HTTP Basic or form login:
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                        .accessDeniedHandler((request, response, e) -> response.setStatus(HttpStatus.FORBIDDEN.value()))
                );

        // JWT filter should *not* throw when no/invalid token for permitted endpoints
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
