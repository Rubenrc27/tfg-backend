package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name = "options")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // CAMBIO CLAVE: Renombrado a optionText para que coincida con el HTML de resultados
    @Column(name = "option_text", nullable = false)
    private String optionText;

    @Column(name = "order_index")
    private Integer orderIndex;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    @JsonIgnore // Evita que la API se vuelva loca enviando la pregunta dentro de la opción
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Question question;
}