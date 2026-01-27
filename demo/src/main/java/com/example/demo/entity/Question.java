package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Usamos esto en vez de JsonIgnore total
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_text", nullable = false)
    private String text;

    // Mantenemos tu Enum, ¡es la mejor práctica!
    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    private QuestionType type;

    @Column(name = "order_index")
    private Integer orderIndex;

    // --- EL CAMBIO CLAVE ---
    // Cambiamos @JsonIgnore por @JsonIgnoreProperties
    // Esto permite que Java lea la encuesta internamente, pero no la envíe al móvil (evita bucles)
    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    @JsonIgnoreProperties("questions")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Survey survey;
    // -----------------------

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Option> options = new ArrayList<>();

    public enum QuestionType {
        OPEN, SINGLE, MULTIPLE
    }

    // Un pequeño truco para que el móvil no se lie con el Enum
    // El móvil espera un String "OPEN" o "SINGLE". Este metodo asegura que lo tenga.
    public String getTypeString() {
        return type.name();
    }
    // AÑADE ESTO PARA BORRAR RESPUESTAS AUTOMÁTICAMENTE
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @ToString.Exclude       // ¡Vital para evitar bucles!
    @JsonIgnore             // ¡Vital para que no explote la API!
    private List<Response> responses;
}