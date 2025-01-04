package com.unibuc.java_project.dto;

import com.unibuc.java_project.model.PaymentMethod;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class PaymentDTO {

    @Min(value = 1, message = "The amount paid must be at least 1.")
    @NotNull(message = "Amount must be provided")
    private Double amountPaid;

    @NotNull(message = "Method of payment must be either 'card' or 'cash'")
    private PaymentMethod method;

    @NotNull(message = "Order ID must be provided")
    private Long orderId;

    // Constructors
    public PaymentDTO() {
    }

    public PaymentDTO(Double amountPaid, PaymentMethod method, Long orderId) {
        this.amountPaid = amountPaid;
        this.method = method;
        this.orderId = orderId;
    }

    // Getters & Setters}
    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}