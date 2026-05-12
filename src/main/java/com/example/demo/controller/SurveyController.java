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
import com.example.demo.entity.User;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Survey> getAllSurveys() {
        return surveyRepository.findAll();
    }

    @GetMapping("/mis-encuestas")
    public List<Survey> getMySurveys(@RequestParam Long userId) {
        return responseRepository.findSurveysByUserId(userId);
    }

    // POST http://localhost:8080/api/surveys/submit
    @PostMapping("/submit")
    public String submitSurvey(@RequestParam Long userId, @RequestBody List<AnswerDTO> answers) {
        User user = userRepository.findById(userId).orElse(null);

        for (AnswerDTO answerDto : answers) {
            // 1. Buscamos la pregunta en BD
            Question question = questionRepository.findById(answerDto.getQuestionId()).orElse(null);

            if (question != null) {
                Response response = new Response();
                response.setQuestion(question);
                response.setSurvey(question.getSurvey());
                response.setUser(user);

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
        }

        return "¡Respuestas guardadas correctamente!";
    }
    @GetMapping("/{id}/stats")
    public Map<String, Object> getSurveyStats(@PathVariable Long id) {
        Survey survey = surveyRepository.findById(id).orElse(null);
        if (survey == null) return null;
        Map<String, Object> stats = new HashMap<>();
        stats.put("surveyId", survey.getId());
        stats.put("title", survey.getTitle());
        List<Response> allResponses = responseRepository.findBySurveyId(id);
        long totalParticipants = allResponses.stream().map(r -> r.getUser() != null ? r.getUser().getId() : null).filter(uid -> uid != null).distinct().count();
        stats.put("totalParticipants", totalParticipants);
        List<Map<String, Object>> questionsStats = survey.getQuestions().stream().map(q -> {
            Map<String, Object> qStat = new HashMap<>();
            qStat.put("questionId", q.getId());
            qStat.put("text", q.getQuestionText());
            qStat.put("type", q.getQuestionType());
            List<Response> qResponses = allResponses.stream().filter(r -> r.getQuestion().getId().equals(q.getId())).collect(Collectors.toList());
            if (q.getQuestionType() != Question.QuestionType.OPEN) {
                Map<Long, Long> optionCounts = qResponses.stream().filter(r -> r.getSelectedOption() != null).collect(Collectors.groupingBy(r -> r.getSelectedOption().getId(), Collectors.counting()));
                List<Map<String, Object>> optionsList = q.getOptions().stream().map(opt -> {
                    Map<String, Object> optMap = new HashMap<>();
                    optMap.put("optionId", opt.getId());
                    optMap.put("text", opt.getOptionText());
                    optMap.put("count", optionCounts.getOrDefault(opt.getId(), 0L));
                    return optMap;
                }).collect(Collectors.toList());
                qStat.put("options", optionsList);
            } else {
                qStat.put("totalAnswers", qResponses.size());
            }
            return qStat;
        }).collect(Collectors.toList());
        stats.put("questions", questionsStats);
        return stats;
    }
}
