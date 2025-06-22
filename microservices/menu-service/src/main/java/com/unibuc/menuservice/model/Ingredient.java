package com.unibuc.menuservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;

@Entity
public class Ingredient {

    public Ingredient() {
    }

    public Ingredient(Long id, String name) {
        this.id = id;
        this.name = name;
        this.stock = 0;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotNull(message = "Ingredient name must not be null.")
    @Size(min = 3, message = "Ingredient name must have at least 3 characters.")
    private String name;

    @Column(nullable = false)
    @NotNull(message = "Stock quantity must not be null.")
    @Min(value = 0, message = "Stock quantity cannot be less than 0.")
    private Integer stock;

    @ManyToMany(mappedBy = "ingredients")
    private List<Dish> dishes;

    // Getters & Setters
    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }
}
