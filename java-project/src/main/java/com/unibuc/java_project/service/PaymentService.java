package com.unibuc.java_project.service;

import com.unibuc.java_project.model.Payment;
import com.unibuc.java_project.model.PaymentMethod;
import com.unibuc.java_project.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    public Payment createPayment(Double amountPaid, PaymentMethod method) {
//        Order order = new Order();
//        order.setId(orderId);

        Payment payment = new Payment(amountPaid, method);
        return paymentRepository.save(payment);
    }

    public Payment getPayment(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }
}
