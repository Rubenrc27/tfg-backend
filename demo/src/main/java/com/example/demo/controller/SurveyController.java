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

    // GET http://localhost:8080/api/surveys/mis-encuestas?userId=5
    @GetMapping("/mis-encuestas")
    public List<Survey> getMyAnsweredSurveys(@RequestParam Long userId) {
        return surveyRepository.findSurveysAnsweredByUser(userId);
    }

    // POST http://localhost:8080/api/surveys/submit
    // POST http://localhost:8080/api/surveys/submit?userId=5
    @PostMapping("/submit")
    public ResponseEntity<?> submitSurvey(
            @RequestParam Long userId,      // <--- 2. Pedimos el ID del usuario que responde
            @RequestBody List<AnswerDTO> answers) {

        // 3. Buscamos al usuario en la BD
        User usuario = userRepository.findById(userId).orElse(null);
        if (usuario == null) {
            return ResponseEntity.badRequest().body("¡Usuario no encontrado!");
        }

        for (AnswerDTO answerDto : answers) {
            Response response = new Response();

            // 4. ¡AQUÍ ESTÁ LA CLAVE! Asignamos el usuario a la respuesta
            response.setUser(usuario);

            // ... resto de tu código igual ...
            Question question = questionRepository.findById(answerDto.getQuestionId()).orElse(null);
            if (question != null) {
                response.setQuestion(question);
                response.setSurvey(question.getSurvey());
            }
            if (answerDto.getOptionId() != null) {
                response.setSelectedOption(optionRepository.findById(answerDto.getOptionId()).orElse(null));
            }
            if (answerDto.getText() != null) {
                response.setResponseText(answerDto.getText());
            }
            responseRepository.save(response);
        }

        return ResponseEntity.ok("¡Respuestas guardadas para el usuario " + usuario.getUsername() + "!");
    }
}