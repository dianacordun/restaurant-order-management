package com.unibuc.java_project.controller;

import com.unibuc.java_project.model.Payment;
import com.unibuc.java_project.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @PostMapping
    public Payment createUser(@RequestBody Payment payment) {
        return paymentRepository.save(payment);
    }
}
