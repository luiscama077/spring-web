package com.estilounico.service;

import com.estilounico.model.Categoria;
import java.util.List;
import java.util.Optional;

public interface CategoriaService {
    
    Categoria guardar(Categoria categoria);
    
    Categoria actualizar(Categoria categoria);
    
    void eliminar(Long id);
    
    Optional<Categoria> buscarPorId(Long id);
    
    List<Categoria> listarTodas();
    
    List<Categoria> listarActivas();
    
    void activarDesactivar(Long id, Boolean activo);
}