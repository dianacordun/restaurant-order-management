package com.unibuc.java_project.dto;

import java.util.List;

public class OrderDTO {

    private Long id;
    private PaymentDTO payment;
    private List<DishDTO> dishes;
    private String status;
    private Double amountToPay;

    // Constructors
    public OrderDTO(Long id, PaymentDTO payment, List<DishDTO> dishes, String status, Double amountToPay) {
        this.id = id;
        this.payment = payment;
        this.dishes = dishes;
        this.status = status;
        this.amountToPay = amountToPay;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentDTO payment) {
        this.payment = payment;
    }

    public List<DishDTO> getDishes() {
        return dishes;
    }

    public void setDishes(List<DishDTO> dishes) {
        this.dishes = dishes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(Double amountToPay) {
        this.amountToPay = amountToPay;
    }
}
