package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users") // Coincide con tu diagrama "users"
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String email; // ✅ Añadido porque sale en tu diagrama

    @Column(nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private String password;

    private String role; // "ADMIN" o "USER"

    // RELACIÓN IMPORTANTE: Un usuario puede tener muchas respuestas
    // Esto conecta con la tabla 'responses' de tu diagrama
    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<Response> responses;
}