package com.unibuc.menuservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    public Order() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id")
    private Long clientId; // Reference to client in user-service

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "order_dish",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id")
    )
    @NotNull(message = "Dishes list cannot be null.")
    @Size(min = 1, message = "An order must contain at least one dish.")
    private List<Dish> dishes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Status must be provided")
    private Status status;

    @Column(name = "amount_to_pay", nullable = false)
    @Min(value = 1, message = "The amount must be at least 1.")
    @NotNull(message = "Amount must be provided")
    private Double amountToPay;

    // Getters & Setters
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setAmountToPay(Double amountToPay) {
        this.amountToPay = amountToPay;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public Long getId() {
        return id;
    }

    public Payment getPayment() {
        return payment;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public Status getStatus() {
        return status;
    }

    public Double getAmountToPay() {
        return amountToPay;
    }
}
