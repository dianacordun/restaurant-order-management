package com.unibuc.java_project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
public class Dish {

    public Dish() {
    }

    public Dish(Long id, String name, Double price, boolean available, List<Ingredient> ingredients) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
        this.available = available;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotNull(message = "Name must not be null.")
    @Size(min = 3, message = "Name must have at least 3 characters.")
    private String name;

    @Column(nullable = false)
    @NotNull(message = "Price must not be null.")
    @Min(value = 0, message = "Price must be greater than or equal to 0.")
    private Double price;

    @ManyToMany(mappedBy = "dishes")
    private List<Order> orders;

    @ManyToMany
    @JoinTable(
            name = "dish_ingredient",
            joinColumns = @JoinColumn(name = "dish_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private List<Ingredient> ingredients;

    @Column(nullable = false)
    @NotNull(message = "Availability must be specified.")
    private boolean available;

    // Getters & Setters
    public @NotNull(message = "Name must not be null.") @Size(min = 3, message = "Name must have at least 3 characters.") String getName() {
        return name;
    }

    public void setName(@NotNull(message = "Name must not be null.") @Size(min = 3, message = "Name must have at least 3 characters.") String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public @NotNull(message = "Price must not be null.") @Min(value = 0, message = "Price must be greater than or equal to 0.") Double getPrice() {
        return price;
    }

    public void setPrice(@NotNull(message = "Price must not be null.") @Min(value = 0, message = "Price must be greater than or equal to 0.") Double price) {
        this.price = price;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NotNull(message = "Availability must be specified.")
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(@NotNull(message = "Availability must be specified.") boolean available) {
        this.available = available;
    }
}

