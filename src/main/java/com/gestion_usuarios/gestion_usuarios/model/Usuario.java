package com.gestion_usuarios.gestion_usuarios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name= "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Usuario {
    
    @Id
    // Identificador
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    // RUN, es unico, no nulo, largo de 8
    @Column(unique= true, nullable= false, length= 8)
    private Integer run;

    // Digito Verificador, no nulo, largo de 1
    @Column(nullable= false, length= 1)
    private String dv;

    // Primer nombre, no nulo, largo de 25
    @Column(nullable = false, length= 25)
    private String pnombre;

    // Segundo nombre, no nulo, largo de 25
    @Column(nullable = true, length= 25)
    private String snombre;

    // Apellido paterno, no nulo, largo de 25
    @Column(nullable = false, length= 25)
    private String appaterno;

    // Apellido materno, no nulo, largo de 25
    @Column(nullable = false, length= 25)
    private String apmaterno;

    // Telefono, es unico, no nulo, largo de 9
    @Column(unique= true, nullable = false, length= 9)
    private Integer telefono;

    // Rol, se setea automaticamente como "Cliente"
    @Column(nullable=false)
    private String rol = "Cliente";

    // Correo, es unico, no nulo, largo de 50
    @Column(unique= true, nullable = false, length= 50)
    private String correo;

    // Contraseña, se pasa en texto plano pero luego se encripta
    @Column(nullable= false, length= 20)
    private String contrasena;

    // Región, no nulo, largo de 30 (La región de Chile con nombre mas largo tiene 27 caracteres)
    @Column(nullable= false, length= 30)
    private String region;

    // Comuna, no nulo, largo de 50 (La comuna de Chile con nombre mas largo tiene 46 caracteres)
    @Column(nullable= false, length= 50)
    private String comuna;

}
