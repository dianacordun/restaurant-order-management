package com.unibuc.java_project.controller;

import com.unibuc.java_project.dto.DishTopDTO;
import com.unibuc.java_project.dto.OrderDTO;
import com.unibuc.java_project.dto.OrderToPlaceDTO;
import com.unibuc.java_project.model.PaymentMethod;
import com.unibuc.java_project.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Orders", description = "Place an order. Update its status or see details of an order. See the top dishes.")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "Place a new order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order placed successfully"),
            @ApiResponse(responseCode = "404", description = "Resource from order not found"),
            @ApiResponse(responseCode = "409", description = "Dishes or ingredients are unavailable.")
    })
    @PostMapping
    public ResponseEntity<?> placeOrder(@Valid @RequestBody OrderToPlaceDTO orderDTO) {
            OrderDTO order  = orderService.placeOrder(orderDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @Operation(summary = "Get an order by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@Parameter(description = "ID of the order to retrieve") @PathVariable Long id) {
            OrderDTO order = orderService.getOrderById(id);
            return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    @Operation(summary = "You can set the status to 0 (PLACED), 1 (PENDING), or 2 (COMPLETED).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "400", description = "Invalid status value")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateOrderStatus(
            @Parameter(description = "ID of the order to update") @PathVariable Long id,
            @Parameter(description = "New status for the order (0 = PLACED, 1 = PENDING, 2 = COMPLETED)") @RequestParam Integer status,
            @Parameter(description = "Payment method for the order (CARD or CASH). Required when status is COMPLETED.") @RequestParam PaymentMethod paymentMethod) {
        OrderDTO order = orderService.updateOrderStatus(id, status, paymentMethod);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }


    @GetMapping("/top-dishes")
    public ResponseEntity<Page<DishTopDTO>> getTopOrderedDishes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "orderCount") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Page<DishTopDTO> topDishes = orderService.getTopOrderedDishes(page, size, sortBy, direction);
        return ResponseEntity.ok(topDishes);
    }
}
