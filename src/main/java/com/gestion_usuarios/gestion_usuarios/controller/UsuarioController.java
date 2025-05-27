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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion_usuarios.gestion_usuarios.model.LoginRequest;
import com.gestion_usuarios.gestion_usuarios.model.LoginResponse;
import com.gestion_usuarios.gestion_usuarios.model.Usuario;
import com.gestion_usuarios.gestion_usuarios.model.UsuarioDTO;
import com.gestion_usuarios.gestion_usuarios.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;

    // Logearse
    @PostMapping("/logear")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        
        try {
        LoginResponse response = usuarioService.login(request);
        return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage()); // devuelve el mensaje de error al cliente
        }
    } 

    // Crea usuarios a partir de datos ingresados en PostMan por parte del CLIENTE
    @PostMapping("/cliente/crear-usuario")
    public Usuario crearUsuarioCliente(@RequestBody Usuario usuario){
        return usuarioService.crearUsuario(usuario);
    }

    // Actualizar un usuario existente por parte del CLEINTE
    @PutMapping("/cliente/actualizar/{id}")
    public Usuario actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        return usuarioService.actualizarUsuario(id, usuario);
    }

    // Elimina por ID
    @DeleteMapping("/cliente/eliminar/{id}")
    public ResponseEntity<Void> eliminarUsuarioCliente(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return null;
    }

    // Crea usuarios a partir de datos ingresados en PostMan por parte de ADMIN
    @PostMapping("/admin/crear-usuario")
    public Usuario crearUsuarioAdmin(@RequestBody Usuario usuario){
        return usuarioService.crearUsuario(usuario);
    }

    // Elimina por ID por parte de ADMIN
    @DeleteMapping("/admin/eliminar/{id}")
    public ResponseEntity<Void> eliminarUsuarioAdmin(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return null;
    }

    // Lista los usuarios existentes por parte de ADMIN
    @GetMapping("/admin/listar-usuarios")
    public List<Usuario> listarUsuarios(){
        return usuarioService.obtenerUsuarios();
    }
    
    // Obtiene un usuario por el ID por parte de ADMIN
    @GetMapping("/admin/obtener-usuario/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorId(id));
    }

}
