package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Para buscar al hacer Login
    Optional<User> findByUsername(String username);

    // Por si queremos comprobar si el email ya existe al registrarse
    Optional<User> findByEmail(String email);
}