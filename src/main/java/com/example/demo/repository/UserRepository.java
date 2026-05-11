package com.example.demo.repository; // Ojo: pon tu paquete real aquí

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Lo usaremos para el Login: Buscar usuario por nombre
    // Devuelve un "Optional" porque puede que el usuario no exista
    Optional<User> findByUsername(String username);

    // Para evitar registros duplicados
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
