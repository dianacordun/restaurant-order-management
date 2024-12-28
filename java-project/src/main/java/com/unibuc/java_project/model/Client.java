package com.unibuc.java_project.model;

import jakarta.persistence.*;
import java.util.List;

import jakarta.validation.constraints.*;

@Entity
public class Client {

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
    private List<Order> orders;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Reservation> reservations;
}
