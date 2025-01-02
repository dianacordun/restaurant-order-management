package com.unibuc.java_project.dto;

public class IngredientForDishDTO {

    private Long id;
    private String name;

    // Constructor, Getters, and Setters
    public IngredientForDishDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

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
}
