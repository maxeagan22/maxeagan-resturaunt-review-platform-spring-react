package com.maxeagan.restaurant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configures Spring Security for the application.
 *
 * - Enforces authentication on all HTTP requests.
 * - Configures OAuth2 Resource Server with JWT token support.
 * - Sets session management to stateless (no HTTP sessions).
 * - Disables CSRF protection (suitable for APIs).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Defines the security filter chain bean that:
     * - Requires authentication for all endpoints.
     * - Uses JWT tokens for OAuth2 resource server authentication.
     * - Disables session state to support stateless REST APIs.
     * - Disables CSRF protection since this is a REST service.
     *
     * @param http the HttpSecurity builder
     * @return configured SecurityFilterChain
     * @throws Exception in case of any configuration errors
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/photos/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    /**
     * Returns the default JWT authentication converter.
     * Override this method to customize how JWT claims are mapped to authorities.
     *
     * @return JwtAuthenticationConverter instance
     */
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        return new JwtAuthenticationConverter();
    }
}
