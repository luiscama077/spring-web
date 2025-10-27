package com.estilounico.service;

import com.estilounico.model.Categoria;
import java.util.List;
import java.util.Optional;

public interface CategoriaService {
    
    Categoria guardar(Categoria categoria);
    
    Categoria actualizar(Categoria categoria);
    
    void eliminar(Long id);
    
    Optional<Categoria> buscarPorId(Long id);
    
    Optional<Categoria> buscarPorNombre(String nombre);
    
    List<Categoria> listarTodas();
    
    List<Categoria> listarActivas();
    
//    List<Categoria> buscarPorNombre(String nombre);
    
    boolean existeNombre(String nombre);
    
    void activarDesactivar(Long id, Boolean activo);
}