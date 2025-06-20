package com.unibuc.menuservice.controller;

import com.unibuc.menuservice.client.UserServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private UserServiceClient userServiceClient;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private String serverPort;

    @Value("${restaurant.menu.max-dishes-per-order:5}")
    private Integer maxDishesPerOrder;

    @Value("${restaurant.menu.currency:EUR}")
    private String currency;

    @GetMapping("/info")
    public Map<String, Object> getServiceInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("service", applicationName);
        info.put("port", serverPort);
        info.put("message", "Menu Service is running");
        info.put("maxDishesPerOrder", maxDishesPerOrder);
        info.put("currency", currency);
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

    @GetMapping("/communicate-with-user")
    public Map<String, Object> communicateWithUserService() {
        Map<String, Object> result = new HashMap<>();
        result.put("caller", "menu-service");
        result.put("message", "Successfully communicating with user-service via Eureka!");

        try {
            Map<String, String> userInfo = userServiceClient.getUserServiceInfo();
            result.put("user-service-response", userInfo);
            result.put("communication-status", "SUCCESS");
        } catch (Exception e) {
            result.put("error", e.getMessage());
            result.put("communication-status", "FAILED");
        }

        return result;
    }

}
