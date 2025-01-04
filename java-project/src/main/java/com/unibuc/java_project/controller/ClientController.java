package com.unibuc.java_project.controller;

import com.unibuc.java_project.dto.ClientDTO;
import com.unibuc.java_project.dto.OrderDTO;
import com.unibuc.java_project.model.Client;
import com.unibuc.java_project.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Clients", description = "See all orders placed by a client or add one.")
@RestController
@RequestMapping("/api/clients")
@Validated
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Operation(summary = "Get all orders by client ID", description = "Fetches all orders of a specific client based on their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @GetMapping("/{clientId}/orders")
    public ResponseEntity<List<OrderDTO>> getClientOrders(
            @Parameter(description = "The ID of the client") @PathVariable Long clientId) {
            List<OrderDTO> orders = clientService.getClientOrders(clientId);
            return ResponseEntity.ok(orders);
    }


    @Operation(summary = "Add a new client", description = "Adds a new client to the system using the provided client data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid client data provided")
    })
    @PostMapping
    public ResponseEntity<ClientDTO> addClient(
            @Parameter(description = "Client data to be added") @Valid @RequestBody ClientDTO clientDTO) {

        Client client = clientService.addClient(clientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ClientDTO(client.getName(), client.getPhoneNumber(), client.getEmail()));
    }
}
