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

    //Esto es para que los datos no se borren entre usuarios
    // --- NUEVOS CAMPOS PARA DATOS PERSONALES ---
    private String fullName; // Nombre real
    private String age;      // Edad (como String para facilitar el controller)

    @Column(columnDefinition = "TEXT")
    private String bio;      // Biografía larga

    private String avatar;   // Emoji o ruta de imagen


    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<Response> responses;
}