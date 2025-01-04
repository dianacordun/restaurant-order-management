package com.unibuc.java_project.controller;

import com.unibuc.java_project.dto.DishCreateDTO;
import com.unibuc.java_project.dto.DishDTO;
import com.unibuc.java_project.model.Dish;
import com.unibuc.java_project.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Dishes", description = "See all or available dishes, update their price, add or delete a dish.")
@RestController
@RequestMapping("/api/dishes")
public class DishController {

    @Autowired
    private DishService dishService;

    @Operation(
            summary = "Get all dishes",
            description = "Fetches a list of all dishes in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of dishes retrieved successfully")
            }
    )
    @GetMapping
    public ResponseEntity<List<DishDTO>> getAllDishes() {
        return ResponseEntity.ok(dishService.getAllDishes());
    }

    @Operation(
            summary = "Get all available dishes",
            description = "Fetches a list of all available dishes in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of dishes retrieved successfully")
            }
    )
    @GetMapping("/available")
    public ResponseEntity<List<DishDTO>> getAllAvailableDishes() {
        return ResponseEntity.ok(dishService.getAllAvailableDishes());
    }

    @Operation(
            summary = "Add a new dish",
            description = "Creates a new dish with the provided name, price, and list of ingredient IDs. The dish is automatically set to available.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Dish created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input provided")
            }
    )
    @PostMapping
    public ResponseEntity<String> addDish(
            @Parameter(description = "DishCreateDTO object containing name, price, and list of ingredient IDs")
            @Valid @RequestBody DishCreateDTO dishDTO) {

        Dish newDish = dishService.addDish(dishDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Dish successfully added.");
    }

    @Operation(
            summary = "Update dish availability",
            description = "Updates the availability of a dish identified by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dish availability updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Dish not found")
            }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateDishAvailability(
            @Parameter(description = "ID of the dish to update") @PathVariable Long id,
            @Parameter(description = "New availability for the dish") @RequestParam Boolean availability) {

        Dish updatedDish = dishService.updateDishAvailability(id, availability);
        return ResponseEntity.ok("Dish successfully updated.");

    }

    @Operation(
            summary = "Delete a dish",
            description = "Deletes a dish identified by its ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Dish deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Dish not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(
            @Parameter(description = "ID of the dish to delete") @PathVariable Long id) {

        dishService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }
}
