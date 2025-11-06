package com.estilounico.service;

import com.estilounico.model.Usuario;
import java.util.Optional;

public interface UsuarioService {
    
    Usuario guardar(Usuario usuario);
    
    Usuario actualizar(Usuario usuario);
    
    Optional<Usuario> buscarPorUsername(String username);
    
    boolean existeUsername(String username);
    
    boolean existeEmail(String email);
    
    void activarDesactivar(Long id);
}