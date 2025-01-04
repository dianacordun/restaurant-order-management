package com.unibuc.java_project.controller;

import com.unibuc.java_project.dto.PaymentDTO;
import com.unibuc.java_project.model.Payment;
import com.unibuc.java_project.service.PaymentService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Payments", description = "Add a new payment entry or see details of a payment.")
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

//    @Operation(
//            summary = "Get all payments",
//            description = "Fetches all payments.",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "List of payments retrieved successfully")
//            }
//    )
//    @GetMapping
//    public List<Payment> getAllPayments() {
//        return paymentRepository.findAll();
//    }

    @Operation(
            summary = "Create a new payment",
            description = "This endpoint allows you to create a new payment in the system.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Payment created successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "404", description = "Order not found")
            }
    )
    @PostMapping
    public ResponseEntity<?> createPayment(
            @Parameter(
                    description = "The payment details including amount, method, etc.",
                    required = true,
                    schema = @Schema(implementation = Payment.class)
            )
            @Valid @RequestBody PaymentDTO paymentDTO
    ) {
            PaymentDTO payment = paymentService.createPayment(paymentDTO);
            return ResponseEntity.status(201).body(payment);
    }

    @Operation(summary = "Retrieve payment by ID", description = "Fetches details of a payment using its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment details fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getPayment(
            @Parameter(
                    name = "id",
                    description = "The ID of the payment to retrieve",
                    required = true,
                    example = "1",
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id
    ) {
            PaymentDTO paymentDTO = paymentService.getPaymentById(id);
            return ResponseEntity.ok(paymentDTO);
    }
}
