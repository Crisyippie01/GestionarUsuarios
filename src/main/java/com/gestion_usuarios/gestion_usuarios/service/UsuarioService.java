package com.gestion_usuarios.gestion_usuarios.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestion_usuarios.gestion_usuarios.model.LoginRequest;
import com.gestion_usuarios.gestion_usuarios.model.LoginResponse;
import com.gestion_usuarios.gestion_usuarios.model.Usuario;
import com.gestion_usuarios.gestion_usuarios.model.UsuarioDTO;
import com.gestion_usuarios.gestion_usuarios.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Para crear un usuario se le entrega un objeto llamado usuario el cual se guarda en la base de datos a través de un metodo 
    // llamado "save(objeto)" el cual existe de manera implicita en el UsuarioRepository.java al ocupar el extend de JPA
    public Usuario crearUsuario(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    // Para eliminar un usuario ocupamos el metodo de deleteById que viene con el extend de JPA en UsuarioRepository.java por lo que
    // Solo nos queda pasarle el ID para que elimine el usuario con ese ID
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    // Metodo para actualizar el usuario por completo, ocupa un usuario nuevo (el que se ingresa desde PostMan) y reemplaza los atributos
    // de comuna, región, numero de contacto y contraseña. (Falta validar por correo)
    public Usuario actualizarUsuario(Long id, Usuario usuarioNuevo){

        // Busca entre los usuarios existentes por ID y le pasa el usuario a la variable de usuarioExistente.
        Usuario usuarioExistente = usuarioRepository.findById(id).orElse(null);

        // Si el usuario existe es diferente a nulo, se hace
        if (usuarioExistente != null) {

            // Se define cuales atributos serán actualizados según los datos ingresados por POST y que reemplazarán al usuarioExistente.
            usuarioExistente.setTelefono(usuarioNuevo.getTelefono());
            usuarioExistente.setRegion(usuarioNuevo.getRegion());
            usuarioExistente.setComuna(usuarioNuevo.getComuna());
            usuarioExistente.setContrasena(usuarioNuevo.getContrasena());
            
            // Guarda el usuario con los atributos ya actualizados.
            return usuarioRepository.save(usuarioExistente);

        } 
        // En caso de que no se encuentre el ID del usuario se cierra el sistema lanzando un mensaje.
        else {
            // throw se ocupa para mandar excepciones de forma manual con un mensaje
            throw new RuntimeException("No se encontro el usuario con ID: "+id);
        }
    }

    // Lista los usuarios existentes en el sistema
    public List<Usuario> obtenerUsuarios(){
        
        // Metodo de UsuarioRepository
        return usuarioRepository.findAll();
    }

    public UsuarioDTO obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return mapearAUsuarioDTO(usuario);
    }

    private UsuarioDTO mapearAUsuarioDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setPnombre(usuario.getPnombre());
        dto.setSnombre(usuario.getSnombre());
        dto.setAppaterno(usuario.getAppaterno());
        dto.setApmaterno(usuario.getApmaterno());
        dto.setRol(usuario.getRol());
        return dto;
    }

    // Metodo para logearse con correo y contraseña, una vez logeado se devuelve el usuario completo
    public LoginResponse login(LoginRequest loginRequest){
        
        // Se busca el usuario en la base de datos por el correo proporcionado
        Optional<Usuario> usuarioOpt = Optional.ofNullable(usuarioRepository.findByCorreo(loginRequest.getCorreo()));

        // Si no se encuentra ningún usuario con ese correo...
        if (usuarioOpt.isEmpty()){
            // ...devuelve mensaje
            throw new RuntimeException("Correo no registrado");
        }

        // Se obtiene el usuario encontrado
        Usuario usuario = usuarioOpt.get();

        // // Se compara la contraseña proporcionada con la almacenada, si no coinciden...
        if (!usuario.getContrasena().equals(loginRequest.getContrasena())){
            
            // ...devuelve mensaje
            throw new RuntimeException("Contraseña invalida");

        }

        // Si pasó los controles, devuelve el usuario existente indicando que has ingresado con exito
        return new LoginResponse("Inicio de Sesión valido", usuario.getRol());
    }
}
