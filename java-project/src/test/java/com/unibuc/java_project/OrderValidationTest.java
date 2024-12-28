package com.unibuc.java_project;

import com.unibuc.java_project.model.Order;
import com.unibuc.java_project.model.Client;
import com.unibuc.java_project.model.Dish;
import com.unibuc.java_project.model.Status;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderValidationTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        // Creeaza validator
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidOrder() {
        // Cream un obiect Order valid
        Order order = new Order();
        order.setClient(new Client());
        order.setDishes(List.of(new Dish()));
        order.setStatus(Status.PLACED);
        order.setAmountToPay(10.0);

        // Validam obiectul
        Set<ConstraintViolation<Order>> violations = validator.validate(order);

        // Nu ar trebui sa existe erori de validare
        assertTrue(violations.isEmpty(), "The order should be valid.");
    }

    @Test
    public void testInvalidOrder() {
        // Cream un obiect Order invalid
        Order order = new Order();
        order.setClient(null);
        order.setDishes(List.of());
        order.setStatus(null);
        order.setAmountToPay(0.0);

        // Validam obiectul
        Set<ConstraintViolation<Order>> violations = validator.validate(order);

        // Ar trebui sa existe erori de validare
        assertFalse(violations.isEmpty(), "The order should be invalid.");
    }
}