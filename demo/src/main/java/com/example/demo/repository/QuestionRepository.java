package com.example.demo.repository;

import com.example.demo.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    // Busca todas las preguntas de una encuesta y las ordena (1, 2, 3...)
    List<Question> findBySurveyIdOrderByOrderIndexAsc(Long surveyId);
}
