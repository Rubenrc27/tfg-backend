package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data; // Esto genera getters y setters automágicos

@Data
@Entity
@Table(name = "users") // Tiene que coincidir con tu tabla SQL
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String role; // 'ADMIN' o 'USER'
}