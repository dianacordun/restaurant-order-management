package com.unibuc.java_project.repository;

import com.unibuc.java_project.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
