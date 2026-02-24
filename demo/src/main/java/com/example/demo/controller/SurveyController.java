package com.example.demo.controller;

import com.example.demo.entity.Question;
import com.example.demo.entity.Response;
import com.example.demo.entity.Survey;
import com.example.demo.dto.AnswerDTO;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private ResponseRepository responseRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private OptionRepository optionRepository;

    @GetMapping
    public List<Survey> getAllSurveys() {
        return surveyRepository.findAll();
    }

    @GetMapping("/mis-encuestas")
    public List<Survey> getMyAnsweredSurveys(@RequestParam Long userId) {
        return surveyRepository.findSurveysAnsweredByUser(userId);
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitSurvey(
            @RequestParam Long userId,
            @RequestBody List<AnswerDTO> answers) {

        // 1. Buscamos al usuario
        User usuario = userRepository.findById(userId).orElse(null);
        if (usuario == null) {
            return ResponseEntity.badRequest().body("¡Usuario no encontrado!");
        }

        // 2. Recorremos la lista de respuestas (aquí se gestionan las múltiples)
        for (AnswerDTO dto : answers) {
            Response response = new Response();
            response.setUser(usuario);

            // 3. Buscamos la pregunta para vincular Survey y Question
            Question question = questionRepository.findById(dto.getQuestionId()).orElse(null);
            if (question != null) {
                response.setQuestion(question);
                response.setSurvey(question.getSurvey());
            }

            // 4. CORRECCIÓN: Usamos directamente el ID (Long) que viene del DTO
            // Esto elimina el error de tipos de tu captura de pantalla
            if (dto.getOptionId() != null) {
                response.setSelectedOption(dto.getOptionId());
            }

            // 5. Gestión de texto libre
            if (dto.getText() != null && !dto.getText().isEmpty()) {
                response.setResponseText(dto.getText());
            }

            // Añadimos la fecha de respuesta
            response.setRespondedAt(LocalDateTime.now());

            // 6. Guardamos (una fila por cada opción si es múltiple)
            responseRepository.save(response);
        }

        return ResponseEntity.ok("Encuesta guardada correctamente para: " + usuario.getUsername());
    }
}