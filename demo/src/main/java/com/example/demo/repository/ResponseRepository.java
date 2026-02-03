package com.example.demo.repository;

import com.example.demo.entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {

    // Este nombre DEBE coincidir con el del HTML
    long countBySelectedOptionId(Long optionId);

    // Para listar respuestas de texto
    List<Response> findByQuestionIdAndResponseTextIsNotNull(Long questionId);
}