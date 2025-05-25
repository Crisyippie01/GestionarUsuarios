package com.gestion_usuarios.gestion_usuarios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
        
        // Llama al servicio para validar las credenciales del usuario
        LoginResponse response = usuarioService.login(request);
        // Si el login es exitoso, devuelve una respuesta HTTP 200 (OK) con el contenido
        return ResponseEntity.ok(response);
    } 

    // Crea usuarios a partir de datos ingresados en PostMan como cliente
    @PostMapping("/cliente/crear-usuario")
    public Usuario crearUsuarioCliente(@RequestBody Usuario usuario){
        return usuarioService.crearUsuario(usuario);
    }

    // Actualizar un usuario existente
    @PutMapping("cliente/actualizar/{id}")
    public Usuario actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        return usuarioService.actualizarUsuario(id, usuario);
    }

    // Crea usuarios a partir de datos ingresados en PostMan como admin
    @PostMapping("/admin/crear-usuario")
    public Usuario crearUsuarioAdmin(@RequestBody Usuario usuario){
        return usuarioService.crearUsuario(usuario);
    }

    // Elimina por ID
    @DeleteMapping("/admin/eliminar/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return null;
    }

    // Lista los usuarios existentes
    @GetMapping("/admin/listar-usuarios")
    public List<Usuario> listarUsuarios(){
        return usuarioService.obtenerUsuarios();
    }
    
    // Obtiene un usuario por el ID
    @GetMapping("/admin/obtener-usuario/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorId(id));
    }

}
