package com.unibuc.userservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

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

}
