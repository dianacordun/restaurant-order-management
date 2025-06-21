package com.unibuc.userservice.controller;

import com.unibuc.userservice.model.Reservation;
import com.unibuc.userservice.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        Optional<Reservation> reservation = reservationService.getReservationById(id);
        return reservation.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Reservation>> getReservationsByClientId(@PathVariable Long clientId) {
        List<Reservation> reservations = reservationService.getReservationsByClientId(clientId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/client/{clientId}/future")
    public ResponseEntity<List<Reservation>> getFutureReservationsByClientId(@PathVariable Long clientId) {
        List<Reservation> reservations = reservationService.getFutureReservationsByClientId(clientId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Reservation>> getReservationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<Reservation> reservations = reservationService.getReservationsByDateRange(start, end);
        return ResponseEntity.ok(reservations);
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        Reservation savedReservation = reservationService.saveReservation(reservation);
        return ResponseEntity.ok(savedReservation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id, @RequestBody Reservation reservation) {
        if (!reservationService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Reservation updatedReservation = reservationService.saveReservation(reservation);
        return ResponseEntity.ok(updatedReservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        if (!reservationService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
