package com.unibuc.java_project.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class ReservationDTO {

    @NotNull(message = "Client ID must not be null.")
    private Long clientId;

    @NotNull(message = "Reservation time must not be null.")
    @Future(message = "Reservation time must be in the future.")
    private LocalDateTime reservationTime;

    @NotNull(message = "Number of guests must not be null.")
    @Positive(message = "Number of guests must be a positive number.")
    private Integer numberOfGuests;

    // Getters and Setters
    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public LocalDateTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalDateTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}