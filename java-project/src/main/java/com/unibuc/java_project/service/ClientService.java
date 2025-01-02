package com.unibuc.java_project.service;

import com.unibuc.java_project.dto.*;
import com.unibuc.java_project.model.Client;
import com.unibuc.java_project.model.Order;
import com.unibuc.java_project.repository.ClientRepository;
import com.unibuc.java_project.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    // Get all orders by client ID
    public List<OrderDTO> getClientOrders(Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new IllegalArgumentException("Client not found"));
        return client.getOrders().stream()
                .map(order -> new OrderDTO(
                        order.getId(),
                        new PaymentDTO(order.getPayment().getAmountPaid(), order.getPayment().getMethod().name(), order.getId()),
                        order.getDishes().stream()
                                .map(dish -> new DishDTO(dish.getId(), dish.getName(), dish.getPrice(), dish.isAvailable(), dish.getIngredients().stream()
                                        .map(ingredient -> new IngredientForDishDTO(ingredient.getId(), ingredient.getName()))
                                        .collect(Collectors.toList())))
                                .collect(Collectors.toList()),
                        order.getStatus().toString(),
                        order.getAmountToPay()))
                .collect(Collectors.toList());
    }

    // Add a new client
    public Client addClient(ClientDTO clientDTO) {
        Client client = new Client();
        client.setName(clientDTO.getName());
        client.setPhoneNumber(clientDTO.getPhoneNumber());
        client.setEmail(clientDTO.getEmail());

        return clientRepository.save(client);
    }
}
