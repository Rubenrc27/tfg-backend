package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "responses")
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "responded_at")
    private LocalDateTime respondedAt = LocalDateTime.now();

    @Column(name = "response_text", columnDefinition = "TEXT")
    private String responseText;

    // RELACIONES

    // ESTO ERA EL ERROR 500: Ahora Java se asegurará de rellenarlo automáticamente
    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    // --- CAMBIO IMPORTANTE ---
    // Hemos quitado "nullable = false".
    // Ahora permite respuestas anónimas (sin usuario) para que no falle tu prueba.
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    // -------------------------

    @ManyToOne
    @JoinColumn(name = "selected_option_id")
    private Option selectedOption;
}