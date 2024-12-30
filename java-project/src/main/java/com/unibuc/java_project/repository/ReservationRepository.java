package com.unibuc.java_project.repository;

import com.unibuc.java_project.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
