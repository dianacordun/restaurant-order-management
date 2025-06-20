package com.unibuc.userservice.controller;

import com.unibuc.userservice.client.MenuServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private MenuServiceClient menuServiceClient;

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

}
