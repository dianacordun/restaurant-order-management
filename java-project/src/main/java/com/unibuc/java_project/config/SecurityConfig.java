package com.unibuc.java_project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.http.SessionCreationPolicy;
import javax.sql.DataSource;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final DataSource dataSource;

    public SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            try {
                JdbcDaoImpl jdbcDao = new JdbcDaoImpl();
                jdbcDao.setDataSource(dataSource);
                jdbcDao.setUsersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username = ?");
                jdbcDao.setAuthoritiesByUsernameQuery("SELECT username, authority FROM authorities WHERE username = ?");
                return jdbcDao.loadUserByUsername(username);
            } catch (UsernameNotFoundException e) {
                throw new UsernameNotFoundException("User not found: " + username);
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Dezactivăm CSRF pentru simplitate
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // Sesiune activă
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/signup", "/resources/**", "/webjars/**", "/static/**", "/css/**", "/js/**", "/images/**")
                        .permitAll() // Acces public la aceste rute
                        .requestMatchers("/orders/**")
                        .authenticated() // Necesită autentificare
                        .anyRequest().authenticated() // Orice altceva necesită autentificare
                )
                .formLogin(form -> form
                        .loginPage("/login") // Pagina personalizată de login
                        .loginProcessingUrl("/login") // URL-ul unde se procesează login
                        .failureUrl("/login?error=true") // În caz de eșec
                        .defaultSuccessUrl("/orders", true) // Redirecționare după login reușit
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL-ul pentru logout
                        .logoutSuccessUrl("/login?logout=true") // Unde se redirecționează după logout
                        .invalidateHttpSession(true) // Distrug sesiunea
                        .clearAuthentication(true) // Șterg autentificarea
                        .permitAll()
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Folosim BCrypt pentru criptarea parolelor
    }

    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http,
            PasswordEncoder passwordEncoder) throws Exception {

        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        authBuilder
                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username = ?")
                .authoritiesByUsernameQuery("SELECT username, authority FROM authorities WHERE username = ?")
                .passwordEncoder(passwordEncoder);

        return authBuilder.build();
    }
}