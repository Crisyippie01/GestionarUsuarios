package com.gestion_usuarios.gestion_usuarios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion_usuarios.gestion_usuarios.DTO.LoginRequest;
import com.gestion_usuarios.gestion_usuarios.DTO.LoginResponse;
import com.gestion_usuarios.gestion_usuarios.DTO.UsuarioDTO;
import com.gestion_usuarios.gestion_usuarios.model.Usuario;
import com.gestion_usuarios.gestion_usuarios.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "Usuarios", description = "Operaciones para gestión de usuarios")
@RestController
@RequestMapping("api/v7/usuarios")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Login de usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login exitoso", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas", content = @Content)
    })
    @PostMapping("/logear")
    public ResponseEntity<?> login(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Login", required = true,
            content = @Content(schema = @Schema(implementation = LoginRequest.class)))
        @org.springframework.web.bind.annotation.RequestBody LoginRequest request) {

        try {
            LoginResponse response = usuarioService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }


    @Operation(summary = "Crear nuevo usuario (Cliente)")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Usuario creado correctamente", content = @Content(schema = @Schema(implementation = Usuario.class))),
    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)})
    @PostMapping("/cliente/crear-usuario")
    public Usuario crearUsuarioCliente(
        @org.springframework.web.bind.annotation.RequestBody Usuario usuario) {
        return usuarioService.crearUsuario(usuario);
    }

    @Operation(summary = "Actualizar usuario (Cliente)")
    @PutMapping("/cliente/actualizar/{id}")
    public Usuario actualizarUsuario(
        @Parameter(description = "ID del usuario a actualizar", required = true)
        @PathVariable Long id,
        @org.springframework.web.bind.annotation.RequestBody Usuario usuario) {
        return usuarioService.actualizarUsuario(id, usuario);
    }

    @Operation(summary = "Eliminar usuario (Cliente)")
    @DeleteMapping("/cliente/eliminar/{id}")
    public ResponseEntity<Void> eliminarUsuarioCliente(
        @Parameter(description = "ID del usuario a eliminar", required = true)
        @PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Crear nuevo usuario (Admin)")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Usuario creado correctamente", content = @Content(schema = @Schema(implementation = Usuario.class))),
    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)})
    @PostMapping("/admin/crear-usuario")
    public Usuario crearUsuarioAdmin(
        @org.springframework.web.bind.annotation.RequestBody Usuario usuario) {
        return usuarioService.crearUsuario(usuario);
    }

    @Operation(summary = "Eliminar usuario (Admin)")
    @DeleteMapping("/admin/eliminar/{id}")
    public ResponseEntity<Void> eliminarUsuarioAdmin(
        @Parameter(description = "ID del usuario a eliminar", required = true)
        @PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Listar todos los usuarios (Admin)")
    @GetMapping("/admin/listar-usuarios")
    public List<UsuarioDTO> listarUsuarios() {
        return usuarioService.obtenerUsuarios();
    }

    @Operation(summary = "Obtener usuario por ID (Admin)")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Usuario encontrado", content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor (usuario no encontrado)", content = @Content)})
    @GetMapping("/admin/obtener-usuario/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuario(
        @Parameter(description = "ID del usuario a consultar", required = true)
        @PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorId(id));
    }

}
