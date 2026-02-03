package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional; // <--- ESTO ES LO QUE FALTA

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Al añadir Optional<>, permites que el Service maneje el error si no encuentra al usuario
    Optional<User> findByUsername(String username);
}