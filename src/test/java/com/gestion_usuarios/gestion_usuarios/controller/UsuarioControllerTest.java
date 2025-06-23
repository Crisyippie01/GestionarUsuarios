package com.gestion_usuarios.gestion_usuarios.controller;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestion_usuarios.gestion_usuarios.DTO.LoginRequest;
import com.gestion_usuarios.gestion_usuarios.DTO.LoginResponse;
import com.gestion_usuarios.gestion_usuarios.DTO.UsuarioDTO;
import com.gestion_usuarios.gestion_usuarios.model.Usuario;
import com.gestion_usuarios.gestion_usuarios.service.UsuarioService;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testLogin() throws Exception {
        LoginRequest loginRequest = new LoginRequest("admin@mail.com", "1234");
        LoginResponse loginResponse = new LoginResponse("Inicio de Sesión valido", "Administrador");

        Mockito.when(usuarioService.login(Mockito.any(LoginRequest.class))).thenReturn(loginResponse);

        mockMvc.perform(post("/api/v7/usuarios/logear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.mensaje").value("Inicio de Sesión valido"))
            .andExpect(jsonPath("$.rol").value("Administrador"));
    }

    @Test
    void testListarUsuarios() throws Exception {
        UsuarioDTO u1 = new UsuarioDTO("Juan", "Pedro", "Pérez", "González", "Cliente", "Valparaíso", "Viña del Mar");
        UsuarioDTO u2 = new UsuarioDTO("Ana", null, "Muñoz", "Soto", "Cliente", "RM", "Santiago");

        Mockito.when(usuarioService.obtenerUsuarios()).thenReturn(Arrays.asList(u1, u2));

        mockMvc.perform(get("/api/v7/usuarios/admin/listar-usuarios"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].pnombre").value("Juan"))
            .andExpect(jsonPath("$[1].pnombre").value("Ana"));
    }

    @Test
    void testObtenerUsuarioPorId() throws Exception {
        UsuarioDTO dto = new UsuarioDTO("Juan", "Pedro", "Pérez", "González", "Cliente", "Valparaíso", "Viña del Mar");

        Mockito.when(usuarioService.obtenerUsuarioPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/v7/usuarios/admin/obtener-usuario/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pnombre").value("Juan"))
            .andExpect(jsonPath("$.comuna").value("Viña del Mar"));
    }

    @Test
    void testCrearUsuarioCliente() throws Exception {
        Usuario usuario = new Usuario(null, 12345678, "K", "Juan", "Pedro", "Pérez", "González", 912345678, "Cliente", "juan@mail.com", "1234", "RM", "Santiago");

        Mockito.when(usuarioService.crearUsuario(Mockito.any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(post("/api/v7/usuarios/cliente/crear-usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pnombre").value("Juan"))
            .andExpect(jsonPath("$.correo").value("juan@mail.com"));
    }

    @Test
    void testActualizarUsuarioCliente() throws Exception {
        Usuario actualizado = new Usuario(1L, 12345678, "K", "Juan", "Pedro", "Pérez", "González", 987654321, "Cliente", "juan@mail.com", "5678", "RM", "Santiago");

        Mockito.when(usuarioService.actualizarUsuario(Mockito.eq(1L), Mockito.any(Usuario.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/v7/usuarios/cliente/actualizar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizado)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.telefono").value(987654321))
            .andExpect(jsonPath("$.contrasena").value("5678"));
    }

    @Test
    void testEliminarUsuarioCliente() throws Exception {
        Mockito.doNothing().when(usuarioService).eliminarUsuario(1L);

        mockMvc.perform(delete("/api/v7/usuarios/cliente/eliminar/1"))
            .andExpect(status().isOk()); 
    }

}
