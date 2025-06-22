package com.unibuc.userservice.controller;

import com.unibuc.userservice.client.MenuServiceClient;
import com.unibuc.userservice.model.User;
import com.unibuc.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private MenuServiceClient menuServiceClient;

    @Autowired
    private UserService userService;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private String serverPort;

    @Value("${security.jwt.secret:default}")
    private String jwtSecret;

    @GetMapping("/info")
    public Map<String, String> getServiceInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("service", applicationName);
        info.put("port", serverPort);
        info.put("message", "User Service is running");
        info.put("jwt-configured", jwtSecret.equals("default") ? "No" : "Yes");
        return info;
    }

    @GetMapping("/health-check")
    public Map<String, String> healthCheck() {
        Map<String, String> health = new HashMap<>();
        health.put("service", applicationName);
        health.put("status", "UP");
        health.put("timestamp", java.time.LocalDateTime.now().toString());
        return health;
    }

    @GetMapping("/communicate-with-menu")
    public Map<String, Object> communicateWithMenuService() {
        Map<String, Object> result = new HashMap<>();
        result.put("caller", "user-service");
        result.put("message", "Successfully communicating with menu-service via Eureka!");

        try {
            Map<String, Object> menuInfo = menuServiceClient.getMenuServiceInfo();
            result.put("menu-service-response", menuInfo);
            result.put("communication-status", "SUCCESS");
        } catch (Exception e) {
            result.put("error", e.getMessage());
            result.put("communication-status", "FAILED");
        }

        return result;
    }

    // User management endpoints
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.getUserByUsername(username);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (userService.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().build();
        }
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @PutMapping("/{username}")
    public ResponseEntity<User> updateUser(@PathVariable String username, @RequestBody User user) {
        if (!userService.existsByUsername(username)) {
            return ResponseEntity.notFound().build();
        }
        user.setUsername(username);
        User updatedUser = userService.saveUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        if (!userService.existsByUsername(username)) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }
}
