package com.unibuc.java_project.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class IngredientDTO {
    private Long id;

    @NotNull(message = "Ingredient name must not be null.")
    @Size(min = 3, message = "Ingredient name must have at least 3 characters.")
    private String name;

    @NotNull(message = "Stock quantity must not be null.")
    @Min(value = 0, message = "Stock quantity cannot be less than 0.")
    private Integer stock;

    // Constructor
    public IngredientDTO(Long id, String name, Integer stock) {
        this.id = id;
        this.name = name;
        this.stock = stock;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}