package com.gestion_usuarios.gestion_usuarios.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gestion_usuarios.gestion_usuarios.DTO.LoginRequest;
import com.gestion_usuarios.gestion_usuarios.DTO.LoginResponse;
import com.gestion_usuarios.gestion_usuarios.DTO.UsuarioDTO;
import com.gestion_usuarios.gestion_usuarios.model.Usuario;
import com.gestion_usuarios.gestion_usuarios.repository.UsuarioRepository;


@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void testCrearUsuario() {
        Usuario usuario = new Usuario(null, 12345678, "K", "Juan", "Pedro", "Pérez", "González", 912345678, "Cliente", "juan@mail.com", "1234", "RM", "Santiago");
        Usuario usuarioGuardado = new Usuario(1L, 12345678, "K", "Juan", "Pedro", "Pérez", "González", 912345678, "Cliente", "juan@mail.com", "1234", "RM", "Santiago");

        when(usuarioRepository.save(usuario)).thenReturn(usuarioGuardado);

        Usuario resultado = usuarioService.crearUsuario(usuario);

        assertNotNull(resultado.getId());
        assertEquals("Juan", resultado.getPnombre());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void testEliminarUsuario() {
        Long id = 1L;

        doNothing().when(usuarioRepository).deleteById(id);

        usuarioService.eliminarUsuario(id);

        verify(usuarioRepository, times(1)).deleteById(id);
    }

    @Test
    void testActualizarUsuario() {
        Long id = 1L;
        Usuario usuarioExistente = new Usuario(id, 12345678, "K", "Juan", "Pedro", "Pérez", "González", 912345678, "Cliente", "juan@mail.com", "1234", "RM", "Santiago");
        Usuario usuarioNuevo = new Usuario(null, 12345678, "K", "Juan", "Pedro", "Pérez", "González", 987654321, "Cliente", "juan@mail.com", "5678", "RM", "Santiago");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario actualizado = usuarioService.actualizarUsuario(id, usuarioNuevo);

        assertEquals(987654321, actualizado.getTelefono());
        assertEquals("5678", actualizado.getContrasena());
        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, times(1)).save(usuarioExistente);
    }

    @Test
    void testObtenerUsuarioPorId() {
        Usuario usuario = new Usuario(1L, 12345678, "K", "Juan", "Pedro", "Pérez", "González", 912345678, "Cliente", "juan@mail.com", "1234", "Valparaíso", "Viña del Mar");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        UsuarioDTO dto = usuarioService.obtenerUsuarioPorId(1L);

        assertEquals("Juan", dto.getPnombre());
        assertEquals("Viña del Mar", dto.getComuna());
    }

    @Test
    void testObtenerUsuarios() {
        Usuario usuario1 = new Usuario(1L, 12345678, "K", "Juan", "Pedro", "Pérez", "González", 912345678, "Cliente", "juan@mail.com", "1234", "Valparaíso", "Viña del Mar");
        Usuario usuario2 = new Usuario(2L, 87654321, "L", "Ana", null, "Muñoz", "Soto", 987654321, "Cliente", "ana@mail.com", "abcd", "RM", "Santiago");

        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario1, usuario2));

        List<UsuarioDTO> lista = usuarioService.obtenerUsuarios();

        assertEquals(2, lista.size());
        assertEquals("Ana", lista.get(1).getPnombre());
    }

    @Test
    void testLogin_valido() {
        Usuario usuario = new Usuario(1L, 12345678, "K", "Juan", "Pedro", "Pérez", "González", 912345678, "Administrador", "admin@mail.com", "1234", "RM", "Santiago");

        when(usuarioRepository.findByCorreo("admin@mail.com")).thenReturn(usuario);

        LoginRequest loginRequest = new LoginRequest("admin@mail.com", "1234");

        LoginResponse response = usuarioService.login(loginRequest);

        assertEquals("Inicio de Sesión valido", response.getMensaje());
        assertEquals("Administrador", response.getRol());
    }

    @Test
    void testLogin_correoNoRegistrado() {
        when(usuarioRepository.findByCorreo("x@mail.com")).thenReturn(null);

        LoginRequest loginRequest = new LoginRequest("x@mail.com", "1234");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.login(loginRequest);
        });

        assertEquals("Correo no registrado", exception.getMessage());
    }

    @Test
    void testLogin_contrasenaInvalida() {
        Usuario usuario = new Usuario(1L, 12345678, "K", "Juan", "Pedro", "Pérez", "González", 912345678, "Cliente", "juan@mail.com", "1234", "RM", "Santiago");

        when(usuarioRepository.findByCorreo("juan@mail.com")).thenReturn(usuario);

        LoginRequest loginRequest = new LoginRequest("juan@mail.com", "9999");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.login(loginRequest);
        });

        assertEquals("Contraseña invalida", exception.getMessage());
    }
}
