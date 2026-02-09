package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        // 1. Buscamos el usuario por nombre
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        // 2. Si no existe, error 401
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
        }

        User user = userOptional.get();

        // 3. Comprobamos la contraseña (¡OJO! Hay que usar matches, no .equals)
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
        }

        // 4. Si todo va bien, preparamos la respuesta JSON
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login exitoso");
        response.put("userId", user.getId());
        response.put("username", user.getUsername());
        response.put("role", user.getRole());

        // Devolvemos los datos al móvil
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> userData) {

        String username = userData.get("username");
        String email = userData.get("email");
        String password = userData.get("password");

        // 1. Validar si existe
        if (userRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "¡El usuario ya existe! 🦆"));
        }

        // 2. Crear usuario
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password)); // Encriptar
        newUser.setRole("USER"); // Rol de la app

        userRepository.save(newUser);

        return ResponseEntity.ok(Map.of("message", "¡Usuario registrado! 🐣"));
    }
}