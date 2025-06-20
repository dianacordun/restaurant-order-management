package com.unibuc.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "menu-service")
public interface MenuServiceClient {

    @GetMapping("/api/menu/info")
    Map<String, Object> getMenuServiceInfo();

    @GetMapping("/api/menu/health-check")
    Map<String, String> getHealthCheck();

}
