package com.unibuc.userservice.model;

import jakarta.persistence.*;
import java.util.List;

import jakarta.validation.constraints.*;

@Entity
public class Client {

    public Client() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Phone number cannot be blank.")
    @Pattern(regexp = "^\\+?\\d{10,15}$", message = "Phone number must be between 10 and 15 digits, optionally starting with a '+'")
    private String phoneNumber;

    @Column(unique = true, nullable = true)
    @Email(message = "Email must be a valid email address")
    private String email;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
