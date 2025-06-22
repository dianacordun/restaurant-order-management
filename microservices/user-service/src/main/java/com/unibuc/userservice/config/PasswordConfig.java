package com.unibuc.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Pentru testare, folosim NoOpPasswordEncoder care permite plain text passwords
        // În producție, folosește BCryptPasswordEncoder
        return NoOpPasswordEncoder.getInstance();
    }
}
