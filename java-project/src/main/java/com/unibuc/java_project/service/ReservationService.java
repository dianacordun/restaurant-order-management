package com.unibuc.java_project.service;

import com.unibuc.java_project.dto.ReservationDTO;
import com.unibuc.java_project.exceptions.ResourceNotFoundException;
import com.unibuc.java_project.model.Client;
import com.unibuc.java_project.model.Reservation;
import com.unibuc.java_project.repository.ReservationRepository;
import com.unibuc.java_project.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ClientRepository clientRepository;

    // Add a new reservation
    @Transactional
    public ReservationDTO createReservation(ReservationDTO reservationCreateDTO) {
        // Fetch the client by ID
        Client client = clientRepository.findById(reservationCreateDTO.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        // Create a new reservation entity
        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setReservationTime(reservationCreateDTO.getReservationTime());
        reservation.setNumberOfGuests(reservationCreateDTO.getNumberOfGuests());

        // Save the reservation and return it
        reservationRepository.save(reservation);
        return reservationCreateDTO;
    }

    // Delete a reservation by ID
    @Transactional
    public void deleteReservation(Long reservationId) {
        if (!reservationRepository.existsById(reservationId)) {
            throw new ResourceNotFoundException("Reservation with id " + reservationId + " not found.");
        }
        reservationRepository.deleteById(reservationId);
    }
}