package com.example.demo.repository;

import com.example.demo.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    // Metodo magico: Spring crea la consulta SQL solo por el nombre del metodo
    // SELECT * FROM surveys WHERE is_active = true;
    List<Survey> findByIsActiveTrue();
}
