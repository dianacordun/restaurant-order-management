package com.unibuc.java_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

@Controller
public class LoginController {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword) {

        // Validare parole
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // Criptare parolă
        String encodedPassword = passwordEncoder.encode(password);

        // Salvează utilizatorul în baza de date
        try (Connection connection = dataSource.getConnection()) {
            // Inserare în tabelul `users`
            String insertUserQuery = "INSERT INTO users (username, password, enabled) VALUES (?, ?, ?)";
            try (PreparedStatement userStmt = connection.prepareStatement(insertUserQuery)) {
                userStmt.setString(1, username);
                userStmt.setString(2, encodedPassword);
                userStmt.setBoolean(3, true);
                userStmt.executeUpdate();
            }

            // Inserare în tabelul `authorities`
            String insertRoleQuery = "INSERT INTO authorities (username, authority) VALUES (?, ?)";
            try (PreparedStatement roleStmt = connection.prepareStatement(insertRoleQuery)) {
                roleStmt.setString(1, username);
                roleStmt.setString(2, "ROLE_USER"); // Adăugăm un rol implicit
                roleStmt.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while registering user", e);
        }

        // Redirect către login după succes
        return "redirect:/login?signupSuccess=true";
    }
}