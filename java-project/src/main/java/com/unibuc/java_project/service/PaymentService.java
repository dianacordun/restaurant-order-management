package com.unibuc.java_project.service;

import com.unibuc.java_project.dto.PaymentDTO;
import com.unibuc.java_project.exceptions.ResourceNotFoundException;
import com.unibuc.java_project.model.Order;
import com.unibuc.java_project.model.Payment;
import com.unibuc.java_project.model.Status;
import com.unibuc.java_project.repository.OrderRepository;
import com.unibuc.java_project.repository.PaymentRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        // Find the order by ID
        Order order = orderRepository.findById(paymentDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() == Status.COMPLETED) {
            throw new IllegalArgumentException("Order has already been completed. Payment not allowed.");
        }

        if (paymentDTO.getAmountPaid() < order.getAmountToPay()) {
            throw new IllegalArgumentException("Insufficient payment amount. The payment must be at least the order's total price of " + order.getAmountToPay());
        }

        Payment payment = new Payment(paymentDTO.getAmountPaid(), paymentDTO.getMethod(), order);
        Payment savedPayment = paymentRepository.save(payment);
        return new PaymentDTO(
                savedPayment.getAmountPaid(),
                savedPayment.getMethod(),
                savedPayment.getOrder().getId()
        );
    }

    // Retrieve a payment by its ID
    public PaymentDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        return new PaymentDTO(
                payment.getAmountPaid(),
                payment.getMethod(),
                payment.getOrder().getId()
        );
    }
}
