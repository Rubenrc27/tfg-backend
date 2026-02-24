package com.example.demo.controller;

import com.example.demo.entity.Option;
import com.example.demo.entity.Question;
import com.example.demo.entity.Survey;
import com.example.demo.entity.User;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- DASHBOARD PRINCIPAL ---
    @GetMapping("/dashboard")
    public String verDashboard(Model model) {
        var encuestas = surveyRepository.findAll();
        model.addAttribute("surveys", encuestas);
        return "dashboard";
    }

    // --- GESTIÓN DE ENCUESTAS ---
    @GetMapping("/encuestas/nueva")
    public String mostrarFormulario(Model model) {
        model.addAttribute("survey", new Survey());
        return "survey_form";
    }

    @PostMapping("/encuestas/guardar")
    public String guardarEncuesta(@ModelAttribute Survey encuesta) {
        if (encuesta.getCreatedAt() == null) {
            encuesta.setCreatedAt(java.time.LocalDateTime.now());
        }
        surveyRepository.save(encuesta);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/encuestas/{id}")
    public String editarEncuesta(@PathVariable Long id, Model model) {
        Survey encuesta = surveyRepository.findById(id).orElse(null);
        model.addAttribute("survey", encuesta);
        model.addAttribute("newQuestion", new Question());
        return "survey_details";
    }

    @GetMapping("/encuestas/borrar/{id}")
    public String borrarEncuesta(@PathVariable Long id) {
        surveyRepository.deleteById(id);
        return "redirect:/admin/dashboard";
    }

    // --- GESTIÓN DE PREGUNTAS ---
    @PostMapping("/encuestas/{id}/preguntas/guardar")
    public String guardarPregunta(@PathVariable Long id, @ModelAttribute Question question) {
        Survey survey = surveyRepository.findById(id).orElse(null);

        // Aseguramos que la pregunta empiece limpia y vinculada a la encuesta
        question.setId(null);
        question.setSurvey(survey);

        // 🦆 ARREGLO PARA EL TIPO DE PREGUNTA:
        // Si el formulario no envió el tipo, le ponemos SINGLE por defecto usando el Enum de la propia clase Question
        if (question.getQuestionType() == null) {
            // Accedemos al Enum interno: Clase.Enum.VALOR
            question.setQuestionType(Question.QuestionType.SINGLE);
        }

        int orden = (survey != null && survey.getQuestions() != null) ? survey.getQuestions().size() + 1 : 1;
        question.setOrderIndex(orden);

        questionRepository.save(question);
        return "redirect:/admin/encuestas/" + id;
    }

    @GetMapping("/preguntas/{id}")
    public String verDetallePregunta(@PathVariable Long id, Model model) {
        Question pregunta = questionRepository.findById(id).orElse(null);
        model.addAttribute("question", pregunta);
        model.addAttribute("newOption", new Option());
        return "question_details";
    }

    // --- GESTIÓN DE OPCIONES (INCLUYE ELIMINAR) ---
    @PostMapping("/preguntas/{id}/opciones/guardar")
    public String guardarOpcion(@PathVariable Long id, @ModelAttribute Option opcion) {
        Question pregunta = questionRepository.findById(id).orElse(null);
        opcion.setId(null);
        opcion.setQuestion(pregunta);
        int orden = (pregunta != null && pregunta.getOptions() != null) ? pregunta.getOptions().size() + 1 : 1;
        opcion.setOrderIndex(orden);
        optionRepository.save(opcion);
        return "redirect:/admin/preguntas/" + id;
    }

    // EL METODO QUE NECESITABAS PARA BORRAR "SPRING" O CUALQUIER OTRA
    @PostMapping("/opciones/{id}/eliminar")
    public String eliminarOpcion(@PathVariable Long id) {
        Option opcion = optionRepository.findById(id).orElse(null);
        if (opcion != null) {
            Long questionId = opcion.getQuestion().getId();
            optionRepository.delete(opcion);
            return "redirect:/admin/preguntas/" + questionId;
        }
        return "redirect:/admin/dashboard";
    }

    // --- RESULTADOS ---
    @GetMapping("/resultados/{id}")
    public String verResultados(@PathVariable Long id, Model model) {
        Survey survey = surveyRepository.findById(id).orElseThrow();
        model.addAttribute("survey", survey);
        model.addAttribute("respo", responseRepository);
        return "resultados_detalle";
    }

    // --- GESTIÓN DE USUARIOS ---
    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("listaUsuarios", userRepository.findAll());
        return "usuarios";
    }

    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(@RequestParam String username, @RequestParam String password) {
        User u = new User();
        u.setUsername(username);
        u.setEmail(username + "@ducksurveys.com");
        u.setPassword(passwordEncoder.encode(password));
        u.setRole("ROLE_ADMIN");
        userRepository.save(u);
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/usuarios/borrar/{id}")
    public String borrarUsuario(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/preguntas/borrar/{id}")
    public String borrarPregunta(@PathVariable Long id) {
        Question pregunta = questionRepository.findById(id).orElseThrow();
        Long surveyId = pregunta.getSurvey().getId();
        questionRepository.delete(pregunta);
        return "redirect:/admin/encuestas/" + surveyId;
    }
}