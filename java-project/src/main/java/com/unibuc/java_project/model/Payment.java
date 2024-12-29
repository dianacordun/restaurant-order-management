package com.unibuc.java_project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
public class Payment {

    public Payment() {
    }

    public Payment(Double amountPaid, PaymentMethod method)
    {
        this.amountPaid = amountPaid;
        this.method = method;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount_paid", nullable = false)
    @Min(value = 1, message = "The amount paid must be at least 1.")
    @NotNull(message = "Amount must be provided")
    private Double amountPaid;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Method of payment must be either 'card' or 'cash'")
    @Column(nullable = false)
    private PaymentMethod method;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public Double getAmountPaid()
    {
        return this.amountPaid;
    }

    public void setAmountPaid(@Min(value = 1, message = "The amount paid must be at least 1.") @NotNull(message = "Amount must be provided") Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public void setMethod(@NotNull(message = "Method of payment must be either 'card' or 'cash'") PaymentMethod method) {
        this.method = method;
    }

    public PaymentMethod getMethod()
    {
        return this.method;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
