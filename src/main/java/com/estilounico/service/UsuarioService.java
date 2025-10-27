package com.estilounico.service;

import com.estilounico.model.Usuario;
import com.estilounico.model.enums.Rol;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    
    Usuario guardar(Usuario usuario);
    
    Usuario actualizar(Usuario usuario);
    
    void eliminar(Long id);
    
    Optional<Usuario> buscarPorId(Long id);
    
    Optional<Usuario> buscarPorUsername(String username);
    
    Optional<Usuario> buscarPorEmail(String email);
    
    List<Usuario> listarTodos();
    
    List<Usuario> listarPorRol(Rol rol);
    
    List<Usuario> listarActivos();
    
    List<Usuario> listarPorRolYActivo(Rol rol, Boolean activo);
    
    boolean existeUsername(String username);
    
    boolean existeEmail(String email);
    
    void activarDesactivar(Long id, Boolean activo);
}