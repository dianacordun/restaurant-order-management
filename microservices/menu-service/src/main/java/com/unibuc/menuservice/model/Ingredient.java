package com.unibuc.menuservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;

@Entity
@Table(name = "ingredients")
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

    // Constructors
    public Ingredient() {
    }

    public Ingredient(String name, Integer stock) {
        this.name = name;
        this.stock = stock;
    }

    public Ingredient(Long id, String name, Integer stock) {
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

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", stock=" + stock +
                '}';
    }
}
