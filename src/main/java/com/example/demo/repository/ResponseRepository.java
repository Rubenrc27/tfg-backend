package com.example.demo.repository;

import com.example.demo.entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResponseRepository extends JpaRepository<Response, Long> {
    // Para sacar todas las respuestas de una encuesta concreta
    List<Response> findBySurveyId(Long surveyId);

    // Métodos necesarios para las estadísticas
    long countBySelectedOptionId(Long optionId);

    // Para obtener las respuestas de texto de una pregunta abierta
    List<Response> findByQuestionIdAndResponseTextIsNotNull(Long questionId);
}
