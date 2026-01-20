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
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller // ¡Ojo! No es @RestController, es @Controller (para webs)
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
        // 1. Buscamos todas las encuestas en la BD
        var encuestas = surveyRepository.findAll();

        // 2. Se las pasamos al HTML
        model.addAttribute("listaEncuestas", encuestas);

        // 3. Le decimos qué archivo HTML cargar (sin el .html)
        return "dashboard";
    }

    // 1. Esto muestra el formulario cuando pulsas el botón verde
    @GetMapping("/encuestas/nueva")
    public String mostrarFormulario() {
        return "survey_form"; // Carga el archivo survey_form.html
    }

    // 2. Esto recibe los datos cuando le das a "Guardar" en el formulario
    @PostMapping("/encuestas/guardar")
    public String guardarEncuesta(@ModelAttribute Survey encuesta) {
        // Le ponemos la fecha y hora actual automáticamente
        encuesta.setCreatedAt(java.time.LocalDateTime.now());

        // Guardamos en la base de datos
        surveyRepository.save(encuesta);

        // Volvemos al dashboard para ver la nueva fila creada
        return "redirect:/admin/dashboard";
    }

    // --- NUEVO: PANTALLA DE EDITOR DE ENCUESTA ---
    @GetMapping("/encuestas/{id}")
    public String editarEncuesta(@PathVariable Long id, Model model) {
        // Buscamos la encuesta por ID
        Survey encuesta = surveyRepository.findById(id).orElse(null);

        // Pasamos la encuesta (y sus preguntas asociadas) a la vista
        model.addAttribute("survey", encuesta);

        // Pasamos una pregunta vacía para el formulario de "Nueva Pregunta"
        model.addAttribute("newQuestion", new Question());

        return "survey_details"; // Crearemos este archivo ahora
    }

    // --- NUEVO: GUARDAR UNA PREGUNTA ---
    @PostMapping("/encuestas/{id}/preguntas/guardar")
    public String guardarPregunta(@PathVariable Long id, @ModelAttribute Question question) {
        // 1. Recuperamos la encuesta a la que pertenece
        Survey survey = surveyRepository.findById(id).get();

        // 2. Asignamos la encuesta a la pregunta (relación Java)
        question.setSurvey(survey);

        // 3. Calculamos el orden (si ya hay 2 preguntas, esta es la 3)
        // (Truco rápido: el tamaño de la lista actual + 1)
        int orden = survey.getQuestions() != null ? survey.getQuestions().size() + 1 : 1;
        question.setOrderIndex(orden);

        // 4. Guardamos la pregunta
        questionRepository.save(question);

        // 5. Redirigimos a la misma pantalla para ver la pregunta añadida
        return "redirect:/admin/encuestas/" + id;
    }

    // 1. Ver la pantalla de gestión de UNA pregunta
    @GetMapping("/preguntas/{id}")
    public String verDetallePregunta(@PathVariable Long id, Model model) {
        // Buscamos la pregunta
        Question pregunta = questionRepository.findById(id).orElse(null);

        model.addAttribute("question", pregunta);
        model.addAttribute("newOption", new Option()); // Para el formulario vacío

        return "question_details"; // Vamos a crear este HTML ahora
    }

    // 2. Guardar una nueva opción
    @PostMapping("/preguntas/{id}/opciones/guardar")
    public String guardarOpcion(@PathVariable Long id, @ModelAttribute Option opcion) {
        Question pregunta = questionRepository.findById(id).get();

        opcion.setQuestion(pregunta);

        // Calculamos el orden (si hay 3 opciones, esta es la 4)
        int orden = pregunta.getOptions() != null ? pregunta.getOptions().size() + 1 : 1;
        opcion.setOrderIndex(orden);

        optionRepository.save(opcion);

        return "redirect:/admin/preguntas/" + id; // Recargamos la misma página
    }
}