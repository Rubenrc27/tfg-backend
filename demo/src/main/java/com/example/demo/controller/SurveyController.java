package com.example.demo.controller;

import com.example.demo.entity.Question;
import com.example.demo.entity.Response;
import com.example.demo.entity.Survey;
import com.example.demo.dto.AnswerDTO;
import com.example.demo.repository.SurveyRepository;
import com.example.demo.repository.ResponseRepository;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.repository.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

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

    // POST http://localhost:8080/api/surveys/submit
    @PostMapping("/submit")
    public String submitSurvey(@RequestBody List<AnswerDTO> answers) {

        for (AnswerDTO answerDto : answers) {
            Response response = new Response();

            // 1. Buscamos la pregunta en BD
            Question question = questionRepository.findById(answerDto.getQuestionId()).orElse(null);

            if (question != null) {
                response.setQuestion(question);

                // --- ¡AQUÍ ESTÁ EL ARREGLO DEL ERROR 500! ---
                // Cogemos la encuesta de la pregunta y se la ponemos a la respuesta
                response.setSurvey(question.getSurvey());
                // --------------------------------------------
            }

            // 2. Opción seleccionada
            if (answerDto.getOptionId() != null) {
                response.setSelectedOption(optionRepository.findById(answerDto.getOptionId()).orElse(null));
            }

            // 3. Texto escrito
            if (answerDto.getText() != null) {
                response.setResponseText(answerDto.getText());
            }

            // 4. Guardar
            responseRepository.save(response);
        }

        return "¡Respuestas guardadas correctamente!";
    }
}