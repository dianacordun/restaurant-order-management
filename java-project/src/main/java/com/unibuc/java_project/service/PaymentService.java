package com.unibuc.java_project.service;

import com.unibuc.java_project.dto.PaymentDTO;
import com.unibuc.java_project.model.Order;
import com.unibuc.java_project.model.Payment;
import com.unibuc.java_project.model.PaymentMethod;
import com.unibuc.java_project.model.Status;
import com.unibuc.java_project.repository.OrderRepository;
import com.unibuc.java_project.repository.PaymentRepository;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        // Find the order by ID
        Order order = orderRepository.findById(paymentDTO.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() == Status.COMPLETED) {
            throw new RuntimeException("Order has already been completed. Payment not allowed.");
        }

        // Create a new Payment entity from DTO
        Payment payment = new Payment(paymentDTO.getAmountPaid(), PaymentMethod.valueOf(paymentDTO.getMethod()), order);
        Payment savedPayment = paymentRepository.save(payment);
        return paymentDTO;
    }

    // Retrieve a payment by its ID
    public PaymentDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        return new PaymentDTO(
                payment.getAmountPaid(),
                payment.getMethod().name(),
                payment.getOrder().getId()
        );
    }
}
