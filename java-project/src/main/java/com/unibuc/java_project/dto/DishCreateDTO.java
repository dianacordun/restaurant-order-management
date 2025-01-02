package com.unibuc.java_project.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class DishCreateDTO {

    @NotNull(message = "Name must not be null.")
    @Size(min = 3, message = "Name must have at least 3 characters.")
    private String name;

    @NotNull(message = "Price must not be null.")
    @Min(value = 0, message = "Price must be greater than or equal to 0.")
    private Double price;

    @NotNull(message = "Ingredient IDs must not be null.")
    private List<Long> ingredientIds;

    // Constructors, getters, and setters
    public DishCreateDTO(List<Long> ingredientIds, Double price, String name) {
        this.ingredientIds = ingredientIds;
        this.price = price;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<Long> getIngredientIds() {
        return ingredientIds;
    }

    public void setIngredientIds(List<Long> ingredientIds) {
        this.ingredientIds = ingredientIds;
    }
}
