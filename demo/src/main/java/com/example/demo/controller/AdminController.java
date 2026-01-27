package com.example.demo.controller;

import com.example.demo.entity.Option;
import com.example.demo.repository.OptionRepository;
import com.example.demo.repository.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.demo.entity.Survey;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.entity.Question;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/dashboard")
    public String verDashboard(Model model) {
        var encuestas = surveyRepository.findAll();
        model.addAttribute("listaEncuestas", encuestas);
        return "dashboard";
    }

    @GetMapping("/encuestas/nueva")
    public String mostrarFormulario() {
        return "survey_form";
    }

    @PostMapping("/encuestas/guardar")
    public String guardarEncuesta(@ModelAttribute Survey encuesta) {
        encuesta.setCreatedAt(java.time.LocalDateTime.now());
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

    @PostMapping("/encuestas/{id}/preguntas/guardar")
    public String guardarPregunta(@PathVariable Long id, @ModelAttribute Question question) {
        Survey survey = surveyRepository.findById(id).orElse(null); // Uso orElse por seguridad

        question.setId(null);
        question.setSurvey(survey);

        int orden = survey.getQuestions() != null ? survey.getQuestions().size() + 1 : 1;
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

    // --- AQUÍ ESTÁ EL ARREGLO DEL ERROR 500 ---
    @PostMapping("/preguntas/{id}/opciones/guardar")
    public String guardarOpcion(@PathVariable Long id, @ModelAttribute Option opcion) {
        // 1. Buscamos la pregunta padre
        Question pregunta = questionRepository.findById(id).orElse(null);

        // 2. ¡IMPORTANTE! Forzamos el ID a null.
        // Esto evita que Hibernate intente actualizar una opción vieja borrada (error StaleObject)
        opcion.setId(null);

        // 3. Asignamos el padre
        opcion.setQuestion(pregunta);

        // 4. Calculamos el orden
        int orden = pregunta.getOptions() != null ? pregunta.getOptions().size() + 1 : 1;
        opcion.setOrderIndex(orden);

        // 5. Guardamos
        optionRepository.save(opcion);

        return "redirect:/admin/preguntas/" + id;
    }
    // --- BORRAR ENCUESTA ---
    @GetMapping("/encuestas/borrar/{id}")
    public String borrarEncuesta(@PathVariable Long id) {
        // Esto borra la encuesta Y, gracias al CascadeType.ALL,
        // también borra las preguntas y opciones asociadas.
        surveyRepository.deleteById(id);

        // Volvemos al listado
        return "redirect:/admin/dashboard";
    }
}