package com.estilounico.repository;

import com.estilounico.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    Optional<Categoria> findByNombre(String nombre);
    
    List<Categoria> findByActivo(Boolean activo);
    
    List<Categoria> findByNombreContainingIgnoreCase(String nombre);
    
    boolean existsByNombre(String nombre);
}