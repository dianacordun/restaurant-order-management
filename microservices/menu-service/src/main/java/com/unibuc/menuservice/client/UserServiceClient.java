package com.unibuc.menuservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/api/users/info")
    Map<String, String> getUserServiceInfo();

    @GetMapping("/api/users/health-check")
    Map<String, String> getHealthCheck();

}
