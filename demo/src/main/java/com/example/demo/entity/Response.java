package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "response") // Conectado a tu tabla SQL
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "responded_at")
    private LocalDateTime respondedAt = LocalDateTime.now();

    @Column(name = "response_text", columnDefinition = "TEXT")
    private String responseText; // Para las respuestas de texto (tipo OPEN)

    // RELACIONES

    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "user_id") // Permite nulos para votos de móviles sin login
    private User user;

    // --- CAMBIO REALIZADO AQUÍ ---
    // Hemos asegurado que se conecte con 'selected_option_id' de tu SQL
    @ManyToOne
    @JoinColumn(name = "selected_option_id")
    private Long selectedOption;
    // -----------------------------
}