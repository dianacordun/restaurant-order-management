package com.unibuc.menuservice.controller;

import com.unibuc.menuservice.client.UserServiceClient;
import com.unibuc.menuservice.dto.DishCreateDTO;
import com.unibuc.menuservice.dto.DishDTO;
import com.unibuc.menuservice.dto.IngredientDTO;
import com.unibuc.menuservice.service.DishService;
import com.unibuc.menuservice.service.IngredientService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private DishService dishService;

    @Autowired
    private IngredientService ingredientService;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private String serverPort;

    @Value("${restaurant.menu.max-dishes-per-order:5}")
    private Integer maxDishesPerOrder;

    @Value("${restaurant.menu.currency:EUR}")
    private String currency;

    private final Counter infoEndpointCounter;
    private final Counter healthCheckCounter;
    private final Counter communicationCounter;

    public MenuController(MeterRegistry meterRegistry) {
        this.infoEndpointCounter = Counter.builder("menu_info_requests_total")
                .description("Total number of menu info requests")
                .register(meterRegistry);
        this.healthCheckCounter = Counter.builder("menu_health_check_requests_total")
                .description("Total number of menu health check requests")
                .register(meterRegistry);
        this.communicationCounter = Counter.builder("menu_user_communication_requests_total")
                .description("Total number of menu-user service communication requests")
                .register(meterRegistry);
    }

    // ================ MONITORING & HEALTH ENDPOINTS ================

    @GetMapping("/info")
    public Map<String, Object> getServiceInfo() {
        infoEndpointCounter.increment();
        Map<String, Object> info = new HashMap<>();
        info.put("service", applicationName);
        info.put("port", serverPort);
        info.put("message", "Menu Service is running - Full dish and ingredient management");
        info.put("maxDishesPerOrder", maxDishesPerOrder);
        info.put("currency", currency);
        return info;
    }

    @GetMapping("/health-check")
    public Map<String, String> healthCheck() {
        healthCheckCounter.increment();
        Map<String, String> health = new HashMap<>();
        health.put("service", applicationName);
        health.put("status", "UP");
        health.put("timestamp", java.time.LocalDateTime.now().toString());
        return health;
    }

    @GetMapping("/communicate-with-user")
    public Map<String, Object> communicateWithUserService() {
        communicationCounter.increment();
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

    // ================ DISH ENDPOINTS ================

    @GetMapping("/dishes")
    public ResponseEntity<List<DishDTO>> getAllDishes() {
        List<DishDTO> dishes = dishService.getAllDishes();
        return ResponseEntity.ok(dishes);
    }

    @GetMapping("/dishes/available")
    public ResponseEntity<List<DishDTO>> getAllAvailableDishes() {
        List<DishDTO> dishes = dishService.getAllAvailableDishes();
        return ResponseEntity.ok(dishes);
    }

    @GetMapping("/dishes/{id}")
    public ResponseEntity<DishDTO> getDishById(@PathVariable Long id) {
        DishDTO dish = dishService.getDishById(id);
        return ResponseEntity.ok(dish);
    }

    @PostMapping("/dishes")
    public ResponseEntity<DishDTO> addDish(@Valid @RequestBody DishCreateDTO dishDTO) {
        DishDTO newDish = dishService.addDish(dishDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newDish);
    }

    @PatchMapping("/dishes/{id}")
    public ResponseEntity<DishDTO> updateDishAvailability(
            @PathVariable Long id,
            @RequestParam Boolean availability) {
        DishDTO updatedDish = dishService.updateDishAvailability(id, availability);
        return ResponseEntity.ok(updatedDish);
    }

    @DeleteMapping("/dishes/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
        dishService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }

    // ================ INGREDIENT ENDPOINTS ================

    @GetMapping("/ingredients")
    public ResponseEntity<List<IngredientDTO>> getAllIngredients() {
        List<IngredientDTO> ingredients = ingredientService.getAllIngredients();
        return ResponseEntity.ok(ingredients);
    }

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<IngredientDTO> getIngredientById(@PathVariable Long id) {
        IngredientDTO ingredient = ingredientService.getIngredientById(id);
        return ResponseEntity.ok(ingredient);
    }

    @PostMapping("/ingredients")
    public ResponseEntity<IngredientDTO> addIngredient(@Valid @RequestBody IngredientDTO ingredientDTO) {
        IngredientDTO newIngredient = ingredientService.addIngredient(ingredientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newIngredient);
    }

    @PutMapping("/ingredients/{id}")
    public ResponseEntity<IngredientDTO> updateIngredient(
            @PathVariable Long id,
            @Valid @RequestBody IngredientDTO ingredientDTO) {
        IngredientDTO updatedIngredient = ingredientService.updateIngredient(id, ingredientDTO);
        return ResponseEntity.ok(updatedIngredient);
    }

    @PatchMapping("/ingredients/{id}/stock")
    public ResponseEntity<IngredientDTO> updateIngredientStock(
            @PathVariable Long id,
            @RequestParam Integer stock) {
        IngredientDTO updatedIngredient = ingredientService.updateStock(id, stock);
        return ResponseEntity.ok(updatedIngredient);
    }

    @DeleteMapping("/ingredients/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long id) {
        ingredientService.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }

}
