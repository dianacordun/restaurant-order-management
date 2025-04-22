package com.unibuc.java_project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unibuc.java_project.annotations.WebMvcTestWithTestProfile;
import com.unibuc.java_project.dto.ClientDTO;
import com.unibuc.java_project.dto.OrderDTO;
import com.unibuc.java_project.exceptions.ResourceNotFoundException;
import com.unibuc.java_project.model.Client;
import com.unibuc.java_project.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTestWithTestProfile(controllers = ClientController.class)
@ExtendWith(MockitoExtension.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    private ClientDTO clientDTO;

    @BeforeEach
    void setUp() {
        clientDTO = new ClientDTO("John Doe", "+1234567890", "john.doe@example.com");
    }

    @Test
    void testGetClientOrders_Success() throws Exception {
        List<OrderDTO> mockOrders = List.of(
                new OrderDTO(1L, null, null, "PENDING", 100.0)
        );

        when(clientService.getClientOrders(1L)).thenReturn(mockOrders);

        // Perform the request and validate the response
        mockMvc.perform(get("/api/clients/1/orders"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    void testGetClientOrders_ClientNotFound() throws Exception {
        when(clientService.getClientOrders(1L)).thenThrow(new ResourceNotFoundException("Client not found"));

        // Perform the request and validate the response
        mockMvc.perform(get("/api/clients/1/orders"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testAddClient_Success() throws Exception {
        Client mockClient = new Client();
        mockClient.setName("John Doe");
        mockClient.setPhoneNumber("+1234567890");
        mockClient.setEmail("john.doe@example.com");

        when(clientService.addClient(any(ClientDTO.class))).thenReturn(mockClient);

        // Perform the request to add a client
        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("+1234567890"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void testAddClient_InvalidData() throws Exception {
        // Modify the clientDTO to make phone number invalid (empty in this case)
        ClientDTO invalidClientDTO = new ClientDTO("John Doe", "", "john.doe@example.com");

        // Perform the request to add a client
        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidClientDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
