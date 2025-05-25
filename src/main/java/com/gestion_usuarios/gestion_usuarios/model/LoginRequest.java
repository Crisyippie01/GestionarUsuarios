package com.gestion_usuarios.gestion_usuarios.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginRequest {
    
    private String correo;
    private String contrasena;

}
