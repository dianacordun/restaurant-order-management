package com.unibuc.userservice.repository;

import com.unibuc.userservice.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByClientId(Long clientId);
    List<Reservation> findByReservationTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Reservation> findByClientIdAndReservationTimeAfter(Long clientId, LocalDateTime time);
}
