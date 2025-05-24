package com.unibuc.java_project.service;

import com.unibuc.java_project.dto.*;
import com.unibuc.java_project.exceptions.ResourceNotFoundException;
import com.unibuc.java_project.exceptions.UnavailableException;
import com.unibuc.java_project.model.*;
import com.unibuc.java_project.repository.ClientRepository;
import com.unibuc.java_project.repository.OrderRepository;
import com.unibuc.java_project.repository.IngredientRepository;
import com.unibuc.java_project.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    public OrderDTO placeOrder(OrderToPlaceDTO orderDTO) {

        Order order = new Order();
        order.setStatus(Status.PLACED);

        // Associate client if client ID is provided
        if (orderDTO.getClientId() != null) {
            Client client = clientRepository.findById(orderDTO.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client not found: " + orderDTO.getClientId()));
            order.setClient(client);
        }

        // Validate and calculate the total amount to pay
        double totalAmountToPay = 0.0;

        // Validate dish availability and calculate amount
        Map<Long, Integer> ingredientUsage = new HashMap<>();
        List<Dish> dishesToAdd = new ArrayList<>();
        for (String dishName : orderDTO.getDishes()) {
            Dish dish = dishRepository.findByName(dishName)
                    .orElseThrow(() -> new ResourceNotFoundException("Dish not found: " + dishName));

            if (!dish.isAvailable()) {
                throw new UnavailableException("Dish is not available: " + dish.getName());
            }

            totalAmountToPay += dish.getPrice();
            dishesToAdd.add(dish);

            // Track ingredient usage for stock validation
            for (Ingredient ingredient : dish.getIngredients()) {
                ingredientUsage.put(ingredient.getId(), ingredientUsage.getOrDefault(ingredient.getId(), 0) + 1);
            }
        }

        for (Map.Entry<Long, Integer> entry : ingredientUsage.entrySet()) {
            Ingredient ingredient = ingredientRepository.findById(entry.getKey())
                    .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found: " + entry.getKey()));

            if (ingredient.getStock() < entry.getValue()) {
                throw new UnavailableException("Not enough stock for ingredient: " + ingredient.getName());
            }
            ingredient.setStock(ingredient.getStock() - entry.getValue());
            ingredientRepository.save(ingredient);
        }

        // Round totalAmountToPay to 2 decimals
        BigDecimal roundedAmountToPay = BigDecimal.valueOf(totalAmountToPay).setScale(2, RoundingMode.HALF_UP);

        order.setAmountToPay(roundedAmountToPay.doubleValue());
        order.setDishes(dishesToAdd);
        order = orderRepository.save(order);

        List<DishDTO> dishesToReturn = dishesToAdd
                .stream()
                .map(dish -> new DishDTO(dish.getId(), dish.getName(), dish.getPrice(), dish.isAvailable(), dish.getIngredients()
                        .stream()
                        .map(ingredient -> new IngredientForDishDTO(ingredient.getId(), ingredient.getName()))
                        .collect(Collectors.toList())))
                .toList();

        // Return the DTO
        return new OrderDTO(
                order.getId(),
                null,
                dishesToReturn,
                order.getStatus().toString(),
                order.getAmountToPay()
        );
    }

    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Check if payment is null
        PaymentDTO paymentDTO = order.getPayment() != null
                ? mapPaymentToDTO(order.getPayment(), order.getId())
                : null;
        List<DishDTO> dishDTOs = mapDishesToDTO(order.getDishes());

        return new OrderDTO(order.getId(), paymentDTO, dishDTOs, order.getStatus().toString(), order.getAmountToPay());
    }

    public OrderDTO updateOrderStatus(Long id, Integer status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        Status statusEnum = mapToStatus(status);
        order.setStatus(statusEnum);
        order = orderRepository.save(order);

        PaymentDTO paymentDTO = null;
        if (order.getPayment() != null) {
            paymentDTO = mapPaymentToDTO(order.getPayment(), order.getId());
        }
        List<DishDTO> dishDTOs = mapDishesToDTO(order.getDishes());

        return new OrderDTO(order.getId(), paymentDTO, dishDTOs, order.getStatus().toString(), order.getAmountToPay());
    }

    public List<DishTopDTO> getTop5MostOrderedDishes() {
        List<Order> orders = orderRepository.findAll();
        Map<Dish, Integer> dishCount = new HashMap<>();

        for (Order order : orders) {
            for (Dish dish : order.getDishes()) {
                dishCount.put(dish, dishCount.getOrDefault(dish, 0) + 1);
            }
        }

        // Sort by count in descending order, limit to top 5, and map to DishTopDTO with positions
        AtomicInteger positionCounter = new AtomicInteger(1);
        return dishCount.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .map(entry -> new DishTopDTO(
                        positionCounter.getAndIncrement(),
                        entry.getValue(),
                        entry.getKey().getId(),
                        entry.getKey().getName(),
                        entry.getKey().getPrice()
                ))
                .collect(Collectors.toList());
    }

    private PaymentDTO mapPaymentToDTO(Payment payment, Long orderId) {
        return new PaymentDTO(
                payment.getAmountPaid(),
                payment.getMethod(),
                orderId
        );
    }

    private List<DishDTO> mapDishesToDTO(List<Dish> dishes) {
        return dishes.stream()
                .map(dish -> new DishDTO(
                        dish.getId(),
                        dish.getName(),
                        dish.getPrice(),
                        dish.isAvailable(),
                        dish.getIngredients().stream()
                                .map(ingredient -> new IngredientForDishDTO(
                                        ingredient.getId(),
                                        ingredient.getName()
                                )).collect(Collectors.toList())
                )).collect(Collectors.toList());
    }

    private Status mapToStatus(Integer status) {
        switch (status) {
            case 0:
                return Status.PLACED;
            case 1:
                return Status.PENDING;
            case 2:
                return Status.COMPLETED;
            default:
                throw new IllegalArgumentException("Invalid status value. Allowed values are 0 (PLACED), 1 (PENDING), 2 (COMPLETED).");
        }
    }
}
