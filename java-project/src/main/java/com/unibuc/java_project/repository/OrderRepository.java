package com.unibuc.java_project.repository;

import com.unibuc.java_project.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
