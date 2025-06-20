package com.unibuc.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        // COMMENT/UNCOMMENT această metodă pentru a controla autentificarea:

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/**").permitAll()      // Allow all API endpoints
                .requestMatchers("/actuator/**").permitAll() // Allow actuator endpoints
                .requestMatchers("/h2-console/**").permitAll() // Allow H2 console
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**") // Disable CSRF for H2 console
                .disable() // Disable CSRF for API endpoints (for testing)
            )
            .headers(headers -> headers
                .frameOptions().sameOrigin() // Allow H2 console frames
            );

        return http.build();
    }

    // Pentru autentificare completă, șterge metoda de mai sus și
    // configurarea din user-service.yml va seta username=admin, password=admin123

}
