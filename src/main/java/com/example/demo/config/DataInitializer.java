package com.example.demo.config;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private ResponseRepository responseRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🦆 INICIANDO GENERACIÓN DE SEED MASIVO...");

        // 1. USUARIOS (15 usuarios + Admin)
        initUsers(15);

        // 2. ENCUESTAS (20 encuestas con preguntas y respuestas)
        if (surveyRepository.count() < 5) { // Si hay pocas, generamos más
            initMassiveSurveys(20);
        }

        System.out.println("🦆 SEED MASIVO COMPLETADO CON ÉXITO.");
    }

    private void initUsers(int count) {
        // Admin siempre presente
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@ducksurveys.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN_SUPREMO");
            userRepository.save(admin);
        }

        for (int i = 1; i <= count; i++) {
            String username = "user" + i;
            if (userRepository.findByUsername(username).isEmpty()) {
                User user = new User();
                user.setUsername(username);
                user.setEmail(username + "@example.com");
                user.setPassword(passwordEncoder.encode("pass123"));
                user.setRole("ROLE_USER");
                userRepository.save(user);
            }
        }
        System.out.println("🦆 " + count + " Usuarios creados.");
    }

    private void initMassiveSurveys(int count) {
        String[] titles = {
            "Clima Laboral 2024", "Preferencias de Café", "Uso de Redes Sociales", 
            "Opinión sobre el Teletrabajo", "Hábitos de Lectura", "Consumo de Videojuegos",
            "Viajes y Turismo", "Alimentación Vegana", "Mascotas en Casa", "Deportes Favoritos",
            "Tecnología y Gadgets", "Cine y Series", "Música Streaming", "Transporte Público",
            "Sostenibilidad", "Educación Online", "Salud Mental", "Ahorro e Inversión",
            "Moda Sostenible", "Futuro de la IA"
        };

        String[] avatars = {"CLASSIC", "COOL", "HEALTHY", "NERD", "INVESTIGATOR", "PARTY", "BUSINESS"};

        List<User> allUsers = userRepository.findAll();
        
        for (int i = 0; i < Math.min(count, titles.length); i++) {
            Survey s = new Survey();
            s.setTitle(titles[i]);
            s.setDescription("Esta es una encuesta automática generada para el seed sobre " + titles[i]);
            s.setDuckAvatar(avatars[random.nextInt(avatars.length)]);
            s = surveyRepository.save(s);

            // Crear 3 preguntas por encuesta
            createQuestionsForSurvey(s, allUsers);
        }
        System.out.println("🦆 " + count + " Encuestas generadas con votos aleatorios.");
    }

    private void createQuestionsForSurvey(Survey survey, List<User> users) {
        // Pregunta 1: SINGLE
        Question q1 = new Question();
        q1.setQuestionText("¿Cuál es tu nivel de satisfacción con " + survey.getTitle() + "?");
        q1.setQuestionType(Question.QuestionType.SINGLE);
        q1.setOrderIndex(1);
        q1.setSurvey(survey);
        questionRepository.save(q1);
        List<Option> ops1 = createOptions(q1, new String[]{"Muy Satisfecho", "Satisfecho", "Neutral", "Insatisfecho"});

        // Pregunta 2: MULTIPLE
        Question q2 = new Question();
        q2.setQuestionText("¿Qué aspectos destacarías?");
        q2.setQuestionType(Question.QuestionType.MULTIPLE);
        q2.setOrderIndex(2);
        q2.setSurvey(survey);
        questionRepository.save(q2);
        List<Option> ops2 = createOptions(q2, new String[]{"Calidad", "Precio", "Atención", "Rapidez", "Diseño"});

        // Pregunta 3: OPEN
        Question q3 = new Question();
        q3.setQuestionText("Danos tu opinión detallada:");
        q3.setQuestionType(Question.QuestionType.OPEN);
        q3.setOrderIndex(3);
        q3.setSurvey(survey);
        questionRepository.save(q3);

        // --- SIMULAR VOTOS ---
        // Hacemos que entre 5 y 12 usuarios voten en esta encuesta
        List<User> voters = new ArrayList<>(users);
        Collections.shuffle(voters);
        int numVoters = 5 + random.nextInt(8); 

        for (int i = 0; i < Math.min(numVoters, voters.size()); i++) {
            User user = voters.get(i);

            // Voto SINGLE
            Response r1 = new Response();
            r1.setSurvey(survey);
            r1.setQuestion(q1);
            r1.setUser(user);
            r1.setSelectedOption(ops1.get(random.nextInt(ops1.size())));
            responseRepository.save(r1);

            // Voto MULTIPLE (selecciona 1 o 2 opciones)
            int optsToSelect = 1 + random.nextInt(2);
            for (int j = 0; j < optsToSelect; j++) {
                Response r2 = new Response();
                r2.setSurvey(survey);
                r2.setQuestion(q2);
                r2.setUser(user);
                r2.setSelectedOption(ops2.get(random.nextInt(ops2.size())));
                responseRepository.save(r2);
            }

            // Voto OPEN (solo algunos escriben)
            if (random.nextBoolean()) {
                Response r3 = new Response();
                r3.setSurvey(survey);
                r3.setQuestion(q3);
                r3.setUser(user);
                r3.setResponseText("Comentario aleatorio del usuario " + user.getUsername() + " sobre esta temática.");
                responseRepository.save(r3);
            }
        }
    }

    private List<Option> createOptions(Question q, String[] texts) {
        List<Option> options = new ArrayList<>();
        for (int i = 0; i < texts.length; i++) {
            Option o = new Option();
            o.setOptionText(texts[i]);
            o.setOrderIndex(i + 1);
            o.setQuestion(q);
            options.add(optionRepository.save(o));
        }
        return options;
    }
}