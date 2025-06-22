package com.unibuc.orderservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/info")
    public Map<String, String> getServiceInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("service", applicationName);
        info.put("port", serverPort);
        info.put("message", "Order Service is running - Ready for order management");
        info.put("status", "UP");
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
}
