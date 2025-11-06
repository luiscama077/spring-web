package com.estilounico.service.impl;

import com.estilounico.model.Usuario;
import com.estilounico.repository.UsuarioRepository;
import com.estilounico.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Override
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
    
    @Override
    public Usuario actualizar(Usuario usuario) {
    	Usuario usuarioExistente = usuarioRepository.findById(usuario.getId())
    	        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    	    
    	    // Si password viene vacío, mantener el actual
    	    if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
    	        usuario.setPassword(usuarioExistente.getPassword());
    	    }
        return usuarioRepository.save(usuario);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username); 
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existeUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existeEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
    
    @Override
    public void activarDesactivar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        usuario.setActivo(!usuario.getActivo());
        usuarioRepository.save(usuario);
    }
}