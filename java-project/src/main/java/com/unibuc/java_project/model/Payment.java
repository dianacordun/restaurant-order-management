package com.unibuc.java_project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Data
public class Payment {

    public Payment(Double amountPaid, PaymentMethod method)
    {
        this.amountPaid = amountPaid;
        this.method = method;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount_paid", nullable = false)
    private Double amountPaid;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Method of payment must be either 'card' or 'cash'")
    @Column(nullable = false)
    private PaymentMethod method;

    public Double getAmountPaid()
    {
        return this.amountPaid;
    }

    public PaymentMethod getMethod()
    {
        return this.method;
    }

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "order_id", referencedColumnName = "id")
//    private Order order; // Legătura One-to-One cu entitatea Order

}
