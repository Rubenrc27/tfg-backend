package com.example.demo.dto;

import lombok.Data;

@Data // Esto pone los getters y setters automáticamente
public class LoginRequest {
    private String username;
    private String password;
}