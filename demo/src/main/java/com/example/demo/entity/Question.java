package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // CAMBIO IMPORTANTE: Renombrado a questionText para coincidir con SQL y HTML
    @Column(name = "question_text", nullable = false)
    private String questionText;

    // CAMBIO IMPORTANTE: Renombrado a questionType //puede fallar *******
    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    @JsonProperty("type") // 🦆 <--- AÑADE ESTO AQUÍ
    private QuestionType questionType;

    @Column(name = "order_index")
    private Integer orderIndex;

    // RELACIÓN CON SURVEY
    // @JsonIgnoreProperties evita que al pedir una pregunta se descargue toda la encuesta de nuevo
    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    @JsonIgnoreProperties("questions")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Survey survey;

    // RELACIÓN CON OPTIONS
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Option> options = new ArrayList<>();

    // RELACIÓN CON RESPONSES
    // @JsonIgnore es vital aquí: Si pides las preguntas al móvil,
    // NO quieres que viajen los miles de votos de otros usuarios.
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private List<Response> responses;

    // Definición del Enum dentro de la clase (como lo tenías)
    public enum QuestionType {
        OPEN, SINGLE, MULTIPLE
    }

    // Helper para APIs que esperan texto plano
    public String getTypeString() {
        return questionType.name();
    }
}