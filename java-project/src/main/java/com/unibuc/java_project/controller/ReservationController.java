package com.unibuc.java_project.controller;

import com.unibuc.java_project.dto.ReservationDTO;
import com.unibuc.java_project.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Reservations", description = "Make or cancel a reservation.")
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    @Operation(
            summary = "Add a new reservation",
            description = "Creates a new reservation with the provided client ID and reservation details.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Reservation added successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "404", description = "Client not found")
            }
    )
    public ResponseEntity<?> addReservation(
            @RequestBody @Valid ReservationDTO reservationCreateDTO) {
            ReservationDTO reservation = reservationService.createReservation(reservationCreateDTO);
            return ResponseEntity.status(201).body(reservation);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a reservation",
            description = "Deletes a reservation by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Reservation deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Reservation not found")
            }
    )
    public ResponseEntity<?> deleteReservation(
            @Parameter(description = "ID of the reservation to delete", required = true)
            @PathVariable Long id) {
            reservationService.deleteReservation(id);
            return ResponseEntity.ok().build();
    }
}
