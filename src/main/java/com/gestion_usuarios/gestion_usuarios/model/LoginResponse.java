package com.gestion_usuarios.gestion_usuarios.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    
    private String mensaje;
    private String rol;

}
