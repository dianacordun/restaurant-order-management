package com.unibuc.menuservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

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

}
