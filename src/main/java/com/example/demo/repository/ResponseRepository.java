package com.example.demo.repository;

import com.example.demo.entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.demo.entity.Survey;

public interface ResponseRepository extends JpaRepository<Response, Long> {
    // Para sacar todas las respuestas de una encuesta concreta
    List<Response> findBySurveyId(Long surveyId);

    // Métodos necesarios para las estadísticas
    long countBySelectedOptionId(Long optionId);

    // Para obtener las respuestas de texto de una pregunta abierta
    List<Response> findByQuestionIdAndResponseTextIsNotNull(Long questionId);
    @Query("SELECT DISTINCT r.survey FROM Response r WHERE r.user.id = :userId")
    List<Survey> findSurveysByUserId(@Param("userId") Long userId);
}
