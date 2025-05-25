package com.gestion_usuarios.gestion_usuarios.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    
    private String pnombre;
    private String snombre;
    private String appaterno;
    private String apmaterno;
    private String rol;

}
