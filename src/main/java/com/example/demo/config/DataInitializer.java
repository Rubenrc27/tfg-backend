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
        // Buscamos si ya existe el usuario 'admin'
        User admin = userRepository.findByUsername("admin").orElse(null);

        if (admin == null) {
            // Si no existe, lo creamos de cero
            admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@ducksurveys.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN_SUPREMO");
            userRepository.save(admin);
            System.out.println("🦆 USUARIO ADMIN CREADO: admin / admin123");
        } else {
            // Si ya existe, nos aseguramos de que tenga el rol y la contraseña correcta
            admin.setRole("ROLE_ADMIN_SUPREMO");
            admin.setPassword(passwordEncoder.encode("admin123"));
            userRepository.save(admin);
            System.out.println("🦆 USUARIO ADMIN ACTUALIZADO: Rol configurado como ROLE_ADMIN_SUPREMO");
        }
    }
}