package com.unibuc.userservice.service;

import com.unibuc.userservice.model.Reservation;
import com.unibuc.userservice.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    public List<Reservation> getReservationsByClientId(Long clientId) {
        return reservationRepository.findByClientId(clientId);
    }

    public List<Reservation> getReservationsByDateRange(LocalDateTime start, LocalDateTime end) {
        return reservationRepository.findByReservationTimeBetween(start, end);
    }

    public List<Reservation> getFutureReservationsByClientId(Long clientId) {
        return reservationRepository.findByClientIdAndReservationTimeAfter(clientId, LocalDateTime.now());
    }

    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return reservationRepository.existsById(id);
    }
}
