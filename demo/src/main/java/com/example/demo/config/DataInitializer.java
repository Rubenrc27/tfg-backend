package com.example.demo.config;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Si no hay usuarios en la base de datos, creamos el ADMIN
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@ducksurveys.com");
            // ¡IMPORTANTE! Encriptamos la contraseña
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");

            userRepository.save(admin);
            System.out.println("🦆 USUARIO ADMIN CREADO: admin / admin123");
        }
        if (userRepository.findByUsername("alumno").isEmpty()) {
            User alumno = new User();
            alumno.setUsername("alumno");
            alumno.setEmail("alumno@duck.com");
            alumno.setPassword(passwordEncoder.encode("1234")); // Contraseña ALUMNO
            alumno.setRole("USER"); // Rol de usuario normal
            userRepository.save(alumno);
            System.out.println("🎓 USUARIO ALUMNO CREADO: alumno / 1234");
        }
    }
}