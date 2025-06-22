package com.unibuc.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Configurare pentru autentificare cu plain text passwords (pentru testare)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/actuator/**").permitAll() // Allow actuator endpoints
                .requestMatchers("/h2-console/**").permitAll() // Allow H2 console
                .anyRequest().authenticated() // Require authentication for all other endpoints
            )
            .httpBasic(httpBasic -> {}) // Enable HTTP Basic Authentication
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**") // Disable CSRF for H2 console
                .disable() // Disable CSRF for API endpoints (for testing)
            )
            .headers(headers -> headers
                .frameOptions().sameOrigin() // Allow H2 console frames
            );

        return http.build();
    }

    // PasswordEncoder bean este deja definit în PasswordConfig.java
    // Configurarea din user-service.yml va seta username=admin, password=admin123
}
