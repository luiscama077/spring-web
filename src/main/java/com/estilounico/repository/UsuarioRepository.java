package com.estilounico.repository;

import com.estilounico.model.Usuario;
import com.estilounico.model.enums.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByUsername(String username);
    
    Optional<Usuario> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    List<Usuario> findByRol(Rol rol);
    
    List<Usuario> findByActivo(Boolean activo);
    
    List<Usuario> findByRolAndActivo(Rol rol, Boolean activo);
}