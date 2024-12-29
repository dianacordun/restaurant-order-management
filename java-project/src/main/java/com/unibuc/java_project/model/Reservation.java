package com.unibuc.java_project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
public class Reservation {

    public Reservation() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    @NotNull(message = "Client must not be null.")
    private Client client;

    @Column(nullable = false)
    @NotNull(message = "Reservation time must not be null.")
    @Future(message = "Reservation time must be in the future.")
    private LocalDateTime reservationTime;

    @Column(nullable = false)
    @NotNull(message = "Number of guests must not be null.")
    @Positive(message = "Number of guests must be a positive number.")
    private Integer numberOfGuests;
    
    // Getters & Setters
    public Long getId() {
        return id;
    }

    public @NotNull(message = "Client must not be null.") Client getClient() {
        return client;
    }

    public void setClient(@NotNull(message = "Client must not be null.") Client client) {
        this.client = client;
    }

    public @NotNull(message = "Reservation time must not be null.") @Future(message = "Reservation time must be in the future.") LocalDateTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(@NotNull(message = "Reservation time must not be null.") @Future(message = "Reservation time must be in the future.") LocalDateTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    public @NotNull(message = "Number of guests must not be null.") @Positive(message = "Number of guests must be a positive number.") Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(@NotNull(message = "Number of guests must not be null.") @Positive(message = "Number of guests must be a positive number.") Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}
