package com.unibuc.menuservice.dto;

import java.util.List;

public class DishDTO {
    private Long id;
    private String name;
    private Double price;
    private boolean available;
    private List<IngredientDTO> ingredients;

    // Constructors
    public DishDTO() {
    }

    public DishDTO(Long id, String name, Double price, boolean available, List<IngredientDTO> ingredients) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.available = available;
        this.ingredients = ingredients;
    }

    // Getters and setters
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public List<IngredientDTO> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientDTO> ingredients) {
        this.ingredients = ingredients;
    }
}
