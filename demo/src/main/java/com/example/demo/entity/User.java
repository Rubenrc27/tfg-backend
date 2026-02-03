package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private String password;

    private String role; // Ej: ROLE_ADMIN_SUPREMO o ROLE_ADMIN

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<Response> responses;
}