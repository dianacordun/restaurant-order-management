package com.unibuc.java_project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unibuc.java_project.annotations.WebMvcTestWithTestProfile;
import com.unibuc.java_project.dto.DishTopDTO;
import com.unibuc.java_project.dto.OrderDTO;
import com.unibuc.java_project.dto.OrderToPlaceDTO;
import com.unibuc.java_project.exceptions.ResourceNotFoundException;
import com.unibuc.java_project.exceptions.UnavailableException;
import com.unibuc.java_project.model.PaymentMethod;
import com.unibuc.java_project.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTestWithTestProfile(controllers = OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    @Test
    void testPlaceOrderSuccess() throws Exception {
        // Prepare the OrderToPlaceDTO and mock service behavior
        OrderToPlaceDTO orderToPlaceDTO = new OrderToPlaceDTO();
        orderToPlaceDTO.setDishes(Arrays.asList("Dish1", "Dish2"));
        orderToPlaceDTO.setClientId(1L);

        OrderDTO mockOrderDTO = new OrderDTO(1L, null, null, "PLACED", 100.0);
        when(orderService.placeOrder(any(OrderToPlaceDTO.class))).thenReturn(mockOrderDTO);

        // Perform the request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderToPlaceDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("PLACED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amountToPay").value(100.0));
    }

    @Test
    void testGetOrderByIdSuccess() throws Exception {
        OrderDTO mockOrderDTO = new OrderDTO(1L, null, null, "PLACED", 100.0);
        when(orderService.getOrderById(anyLong())).thenReturn(mockOrderDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("PLACED"));
    }

    @Test
    void testGetOrderByIdNotFound() throws Exception {
        when(orderService.getOrderById(anyLong())).thenThrow(new ResourceNotFoundException("Order not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/{id}", 99L))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(containsString("Order not found")));
    }

    @Test
    void testUpdateOrderStatusSuccess() throws Exception {
        OrderDTO mockOrderDTO = new OrderDTO(1L, null, null, "PENDING", 100.0);
        when(orderService.updateOrderStatus(anyLong(), any(Integer.class), any(PaymentMethod.class)))
                .thenReturn(mockOrderDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/orders/{id}", 1L)
                        .param("status", "1")
                        .param("paymentMethod", "CASH")) // Add paymentMethod as parameter
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("PENDING"));
    }

    @Test
    void testUpdateOrderStatusOrderNotFound() throws Exception {
        when(orderService.updateOrderStatus(anyLong(), any(Integer.class), any(PaymentMethod.class)))
                .thenThrow(new ResourceNotFoundException("Order not found"));

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/orders/{id}", 99L)
                        .param("status", "1")
                        .param("paymentMethod", "CARD")) // Add paymentMethod as parameter
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(containsString("Order not found")));
    }

    @Test
    void testGetTop5MostOrderedDishes() throws Exception {
        // Simulate response
        when(orderService.getTop5MostOrderedDishes()).thenReturn(Arrays.asList(
                new DishTopDTO(1, 10, 1L, "Dish1", 20.0),
                new DishTopDTO(2, 8, 2L, "Dish2", 15.0)
        ));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/top-dishes"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Dish1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Dish2"));
    }

    @Test
    void testPlaceOrderDishNotFound() throws Exception {
        OrderToPlaceDTO orderToPlaceDTO = new OrderToPlaceDTO();
        orderToPlaceDTO.setDishes(Arrays.asList("Dish1", "DishNotAvailable"));
        orderToPlaceDTO.setClientId(1L);

        when(orderService.placeOrder(any(OrderToPlaceDTO.class)))
                .thenThrow(new ResourceNotFoundException("Dish not found: DishNotAvailable"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderToPlaceDTO)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(containsString("Dish not found: DishNotAvailable")));
    }

    @Test
    void testPlaceOrderDishUnavailable() throws Exception {
        OrderToPlaceDTO orderToPlaceDTO = new OrderToPlaceDTO();
        orderToPlaceDTO.setDishes(List.of("Dish1"));
        orderToPlaceDTO.setClientId(1L);

        when(orderService.placeOrder(any(OrderToPlaceDTO.class)))
                .thenThrow(new UnavailableException("Dish is not available: Dish1"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderToPlaceDTO)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.content().string(containsString("Dish is not available: Dish1")));
    }

    @Test
    void testPlaceOrderUnavailableIngredients() throws Exception {
        // Prepare the OrderToPlaceDTO with a list of dishes
        OrderToPlaceDTO orderToPlaceDTO = new OrderToPlaceDTO();
        orderToPlaceDTO.setDishes(Arrays.asList("Dish1", "Dish2"));
        orderToPlaceDTO.setClientId(1L);

        // Simulate the scenario where an ingredient is unavailable (mock the service behavior)
        when(orderService.placeOrder(any(OrderToPlaceDTO.class)))
                .thenThrow(new UnavailableException("Not enough stock for ingredient: Ingredient1"));

        // Perform the request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderToPlaceDTO)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.content().string(containsString("Not enough stock for ingredient: Ingredient1")));
    }
}