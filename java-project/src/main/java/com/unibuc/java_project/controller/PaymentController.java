package com.unibuc.java_project.controller;

import com.unibuc.java_project.model.Payment;
import com.unibuc.java_project.repository.PaymentRepository;
import com.unibuc.java_project.service.PaymentService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentService paymentService;

    @Operation(summary = "Get all payments", description = "Fetches all payments.")
    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Operation(
            summary = "Create a new payment",
            description = "This endpoint allows you to create a new payment in the system."
    )
    @ApiResponse(responseCode = "201", description = "Payment created successfully")
    @PostMapping
    public Payment createPayment(
            @Parameter(
                    description = "The payment details including amount, method, etc.",
                    required = true,
                    schema = @Schema(implementation = Payment.class)
            )
            @Valid @RequestBody Payment payment
    ) {
        return paymentService.createPayment(payment.getAmountPaid(), payment.getMethod());
    }

    @Operation(summary = "Retrieve payment by ID", description = "Fetches details of a payment using its unique ID.")
    @GetMapping("/{id}")
    public Payment getPayment(
            @Parameter(
                    name = "id",
                    description = "The ID of the payment to retrieve",
                    required = true,
                    example = "1",
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id
    ) {
        return paymentService.getPayment(id);
    }
}
