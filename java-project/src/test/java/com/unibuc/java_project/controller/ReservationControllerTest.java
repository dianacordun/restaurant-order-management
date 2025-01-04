package com.unibuc.java_project.controller;

import com.unibuc.java_project.dto.ReservationDTO;
import com.unibuc.java_project.exceptions.ResourceNotFoundException;
import com.unibuc.java_project.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    private ReservationDTO reservationDTO;

    @BeforeEach
    void setUp() {
        reservationDTO = new ReservationDTO();
        reservationDTO.setClientId(1L);
        reservationDTO.setReservationTime(LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS));  // Future time
        reservationDTO.setNumberOfGuests(4);
    }

    @Test
    public void addReservation_ShouldReturnCreatedReservation() throws Exception {
        // Mock the service to return the same reservation DTO
        when(reservationService.createReservation(any(ReservationDTO.class)))
                .thenReturn(reservationDTO);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clientId\":1,\"reservationTime\":\"" + reservationDTO.getReservationTime().toString() + "\",\"numberOfGuests\":4}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clientId").value(1L))
                .andExpect(jsonPath("$.reservationTime").value(reservationDTO.getReservationTime().toString()))
                .andExpect(jsonPath("$.numberOfGuests").value(4));
    }

    @Test
    public void addReservation_ShouldReturnNotFound_WhenClientNotFound() throws Exception {
        // Mock the service to throw an exception when client is not found
        when(reservationService.createReservation(any(ReservationDTO.class)))
                .thenThrow(new ResourceNotFoundException("Client not found"));

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clientId\":99,\"reservationTime\":\"" + reservationDTO.getReservationTime().toString() + "\",\"numberOfGuests\":4}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Client not found")));
    }

    @Test
    public void deleteReservation_ShouldReturnOk() throws Exception {
        // Mock the service to delete the reservation successfully
        doNothing().when(reservationService).deleteReservation(1L);

        mockMvc.perform(delete("/api/reservations/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteReservation_ShouldReturnNotFound_WhenReservationNotFound() throws Exception {
        // Mock the service to throw an exception when reservation is not found
        doThrow(new ResourceNotFoundException("Reservation with id 99 not found."))
                .when(reservationService).deleteReservation(99L);

        mockMvc.perform(delete("/api/reservations/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Reservation with id 99 not found.")));
    }

}
