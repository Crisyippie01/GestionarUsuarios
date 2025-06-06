package com.gestion_usuarios.gestion_usuarios.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestion_usuarios.gestion_usuarios.DTO.LoginRequest;
import com.gestion_usuarios.gestion_usuarios.DTO.LoginResponse;
import com.gestion_usuarios.gestion_usuarios.DTO.UsuarioDTO;
import com.gestion_usuarios.gestion_usuarios.model.Usuario;
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
    public List<UsuarioDTO> obtenerUsuarios(){
        
        // Crea dos listas, una que contiene todos los usuarios y otra vacia de la clase DTO
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<UsuarioDTO> usuariosDTO = new ArrayList<>();
        
        // Recorre todos los usuarios y agrega a la lista de la clase DTO todos los usuarios 
        // Sin antes seleccionar solo lo que la clase DTO ocupa a través del metodo mapearAUsuarioDTO
        for (Usuario usuario : usuarios) {
            usuariosDTO.add(mapearAUsuarioDTO(usuario));
        }

        // Metodo de UsuarioRepository
        return usuariosDTO;
    }

    // Metodo que devuelve el UsuarioDTO
    public UsuarioDTO obtenerUsuarioPorId(Long id) {

        // Busco el Usuario por ID y si no lo encuentra...
        Usuario usuario = usuarioRepository.findById(id)
            // ...Retorna el mensaje
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Llama el metodo que transforma el usuario a un usuarioDTO con los datos necesarios
        return mapearAUsuarioDTO(usuario);
    }

    // Metodo para mapear un usuario DTO y luego pasarlo al obtenerUsuarioPorID
    private UsuarioDTO mapearAUsuarioDTO(Usuario usuario) {
        
        // Creas un objeto de la clase UsuarioDTO
        UsuarioDTO dto = new UsuarioDTO();

        // Entregas los atributos que entrega el usuario
        dto.setPnombre(usuario.getPnombre());
        dto.setSnombre(usuario.getSnombre());
        dto.setAppaterno(usuario.getAppaterno());
        dto.setApmaterno(usuario.getApmaterno());
        dto.setRol(usuario.getRol());
        dto.setRegion(usuario.getRegion());
        dto.setComuna(usuario.getComuna());

        // Retornas el UsuarioDTO
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
