package com.unibuc.menuservice.dto;

public class IngredientDTO {
    private Long id;
    private String name;
    private Integer stock;

    // Constructors
    public IngredientDTO() {
    }

    public IngredientDTO(Long id, String name, Integer stock) {
        this.id = id;
        this.name = name;
        this.stock = stock;
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

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
