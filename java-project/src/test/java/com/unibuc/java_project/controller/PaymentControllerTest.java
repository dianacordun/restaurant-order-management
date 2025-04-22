package com.unibuc.java_project.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unibuc.java_project.annotations.WebMvcTestWithTestProfile;
import com.unibuc.java_project.dto.PaymentDTO;
import com.unibuc.java_project.exceptions.ResourceNotFoundException;
import com.unibuc.java_project.model.PaymentMethod;
import com.unibuc.java_project.repository.PaymentRepository;
import com.unibuc.java_project.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTestWithTestProfile(controllers = PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService paymentService;

    @MockitoBean
    private PaymentRepository paymentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createPayment_ShouldReturnCreatedPayment() throws Exception {
        // Mock data
        PaymentDTO requestPayment = new PaymentDTO(100.0, PaymentMethod.CASH, 1L);
        PaymentDTO responsePayment = new PaymentDTO(100.0, PaymentMethod.CASH, 1L);

        // Mock service behavior
        Mockito.when(paymentService.createPayment(Mockito.any(PaymentDTO.class)))
                .thenReturn(responsePayment);

        // Perform POST request and validate response
        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestPayment)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amountPaid", is(100.0)))
                .andExpect(jsonPath("$.method", is("CASH")))
                .andExpect(jsonPath("$.orderId", is(1)));
    }

    @Test
    public void createPayment_ShouldReturnNotFound_WhenOrderNotFound() throws Exception {
        // Mock behavior for order not found
        Mockito.when(paymentService.createPayment(Mockito.any(PaymentDTO.class)))
                .thenThrow(new ResourceNotFoundException("Order not found"));

        // Perform POST request and validate error response
        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PaymentDTO(100.0, PaymentMethod.CARD, 99L))))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Order not found")));
    }

    @Test
    public void createPayment_ShouldReturnBadRequest_WhenPaymentAmountIsInsufficient() throws Exception {
        // PaymentDTO with an insufficient amount
        PaymentDTO insufficientPaymentDTO = new PaymentDTO(50.0, PaymentMethod.CARD, 1L); // Amount less than the order total

        Mockito.when(paymentService.createPayment(Mockito.any(PaymentDTO.class)))
                .thenThrow(new IllegalArgumentException("Insufficient payment amount. The payment must be at least the order's total price of 100.0"));

        // Perform POST request and validate error response
        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(insufficientPaymentDTO)))
                .andExpect(status().isBadRequest()) // Expect HTTP 400 Bad Request
                .andExpect(content().string(containsString("Insufficient payment amount")))
                .andExpect(content().string(containsString("at least the order's total price of 100.0"))); // Verify error message contains expected details
    }


    @Test
    public void getPayment_ShouldReturnPaymentDetails() throws Exception {
        // Mock data
        PaymentDTO paymentDTO = new PaymentDTO(200.0, PaymentMethod.CASH, 2L);

        // Mock service behavior
        Mockito.when(paymentService.getPaymentById(2L)).thenReturn(paymentDTO);

        // Perform GET request and validate response
        mockMvc.perform(get("/api/payments/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amountPaid", is(200.0)))
                .andExpect(jsonPath("$.method", is("CASH")))
                .andExpect(jsonPath("$.orderId", is(2)));
    }

    @Test
    public void getPayment_ShouldReturnNotFound_WhenPaymentNotFound() throws Exception {
        // Mock behavior for payment not found
        Mockito.when(paymentService.getPaymentById(99L))
                .thenThrow(new ResourceNotFoundException("Payment not found"));

        // Perform GET request and validate error response
        mockMvc.perform(get("/api/payments/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Payment not found")));
    }

}