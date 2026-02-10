package com.example.demo.repository;

import com.example.demo.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    // "Selecciona encuestas DIFERENTES (distinct) uniendo preguntas y respuestas
    // donde el usuario de la respuesta sea el ID que te paso"
    @Query("SELECT DISTINCT s FROM Survey s JOIN s.questions q JOIN q.responses r WHERE r.user.id = :userId")
    List<Survey> findSurveysAnsweredByUser(@Param("userId") Long userId);
}
