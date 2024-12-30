package com.unibuc.java_project.controller;

import com.unibuc.java_project.dto.IngredientDTO;
import com.unibuc.java_project.service.IngredientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
@Validated
public class IngredientController {

    @Autowired
    private IngredientService ingredientService;

    @GetMapping
    @Operation(
            summary = "Get all ingredients",
            description = "Retrieve a list of all ingredients",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of ingredients retrieved successfully")
            }
    )
    public ResponseEntity<List<IngredientDTO>> getAllIngredients() {
        return ResponseEntity.ok(ingredientService.getAllIngredients());
    }

    @PatchMapping("/{id}/stock")
    @Operation(
            summary = "Update the stock of an ingredient",
            description = "Update the stock quantity for a specific ingredient by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Stock updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Ingredient not found")
            }
    )
    public ResponseEntity<IngredientDTO> updateStock(
            @Parameter(description = "ID of the ingredient to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "New stock quantity", required = true)
            @RequestParam Integer stock) {

        try {
            IngredientDTO updatedIngredient = ingredientService.updateStock(id, stock);
            return ResponseEntity.ok(updatedIngredient);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}