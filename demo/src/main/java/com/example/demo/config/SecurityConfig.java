package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() // Deja pasar los estilos
                        .anyRequest().authenticated() // Todo lo demás requiere login
                )
                .formLogin((form) -> form
                        .loginPage("/login") // <--- ¡ESTA ES LA CLAVE!
                        .permitAll()
                        .defaultSuccessUrl("/admin/dashboard", true)
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }

    // El encriptador de contraseñas (Nadie debe saber qué contraseña real tienes)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}