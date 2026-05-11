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
                        // 1. Permitir acceso a estilos y recursos estáticos sin login
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

                        // 2. SOLO el ADMIN_SUPREMO puede entrar a gestionar usuarios
                        // Nota: En la BD el rol debe ser 'ROLE_ADMIN_SUPREMO'
                        .requestMatchers("/admin/usuarios/**").hasRole("ADMIN_SUPREMO")

                        // 3. Tanto el Supremo como los Admins normales pueden usar el ERP (encuestas, dashboard, etc.)
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN_SUPREMO", "ADMIN")

                        // 4. Cualquier otra ruta requiere estar autenticado
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/admin/dashboard", true) // Al entrar, directos al dashboard
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout") // Al salir, volvemos al login con mensaje
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    // El encriptador necesario para que el AdminController pueda guardar usuarios
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}