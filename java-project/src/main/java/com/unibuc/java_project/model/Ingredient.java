package com.unibuc.java_project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;

@Entity
public class Ingredient {

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
}
