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
                // 1. (DE TU RAMA) DESACTIVAR CSRF PARA LA API
                // Vital para que la App Flutter pueda enviar POSTs sin líos de tokens
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))

                .authorizeHttpRequests((requests) -> requests
                        // 2. Recursos estáticos (CSS, JS) públicos para todos
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

                        // 3. (DE TU RAMA) API PÚBLICA PARA LA APP
                        // Permite que Flutter haga login y pida encuestas
                        .requestMatchers("/api/**").permitAll()

                        // 4. (DEL MAIN) SEGURIDAD DEL ERP WEB
                        // Solo el ADMIN_SUPREMO toca usuarios
                        .requestMatchers("/admin/usuarios/**").hasRole("ADMIN_SUPREMO")
                        // Admins y Supremo entran al resto del panel
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN_SUPREMO", "ADMIN")

                        // 5. El resto bloqueado
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        // (DEL MAIN) Al entrar en web, ir directo al dashboard
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    // El encriptador de contraseñas (necesario para ambos)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}