package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.config.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.demo.repository.UserRepository;
import com.example.demo.entity.User;
import java.util.Optional;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Validar credenciales
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // Guardar en el contexto de seguridad (opcional con JWT pero buena práctica)
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generar Token JWT
            String jwt = jwtUtils.generateToken(authentication);

            // Devolver respuesta con el Token
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("type", "Bearer");
            response.put("username", authentication.getName());
            response.put("role", authentication.getAuthorities().toString());
            
            Optional<User> user = userRepository.findByUsername(authentication.getName());
            if (user.isPresent()) {
                response.put("userId", user.get().getId());
                response.put("id", user.get().getId());
            }
            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Usuario o contraseña incorrectos");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
}