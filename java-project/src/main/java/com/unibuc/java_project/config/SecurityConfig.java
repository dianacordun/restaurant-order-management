package com.unibuc.java_project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Autowired
    private DataSource dataSource;


    public SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        return username -> {
            try {
                JdbcDaoImpl jdbcDao = new JdbcDaoImpl();
                jdbcDao.setDataSource(dataSource);
                jdbcDao.setUsersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username = ?");
                jdbcDao.setAuthoritiesByUsernameQuery("SELECT username, authority FROM authorities WHERE username = ?");

                UserDetails user = jdbcDao.loadUserByUsername(username);
                System.out.println("DEBUG: Parola stocată = " + user.getPassword()); // Parola criptată
                return user;
            } catch (UsernameNotFoundException e) {
                throw e;
            }
        };
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Dezactivăm CSRF, dar se poate activa dacă este necesar
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/resources/**", "/webjars/**", "/static/**", "/css/**", "/js/**", "/images/**").permitAll() // Rute publice
                        .requestMatchers("/orders/**").authenticated() // Endpointuri protejate
                        .anyRequest().permitAll() // Toate celelalte vor fi permise
                )
                .formLogin(form -> form
                        .loginPage("/login") // Pagina de login personalizată
                        .failureUrl("/login?error=true") // Redirecționare în caz de eroare
                        .defaultSuccessUrl("/orders", true) // Redirecționare după login reușit
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // Configurare logout
                        .logoutSuccessUrl("/login") // Redirecționare după logout
                        .permitAll()
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Configurare BCrypt pentru criptarea parolelor
    }

    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http,
            PasswordEncoder passwordEncoder,
            DataSource dataSource) throws Exception {

        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        authBuilder
                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username = ?")
                .authoritiesByUsernameQuery("SELECT username, authority FROM authorities WHERE username = ?")
                .passwordEncoder(passwordEncoder());

        return authBuilder.build();
    }


}