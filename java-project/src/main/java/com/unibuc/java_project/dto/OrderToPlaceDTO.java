package com.unibuc.java_project.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class OrderToPlaceDTO {

    @NotNull(message = "Dishes list cannot be null.")
    @Size(min = 1, message = "An order must contain at least one dish.")
    private List<String> dishes;

    private Long clientId;

    // Constructor
    public OrderToPlaceDTO(List<String> dishes, Long clientId) {
        this.dishes = dishes;
        this.clientId = clientId;
    }

    public OrderToPlaceDTO() {

    }

    // Getters & Setters
    public List<String> getDishes() {
        return dishes;
    }

    public void setDishes(List<String> dishes) {
        this.dishes = dishes;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
