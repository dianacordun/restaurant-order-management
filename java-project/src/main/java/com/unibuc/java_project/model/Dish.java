package com.unibuc.java_project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
public class Dish {

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
}

