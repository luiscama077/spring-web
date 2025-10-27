package com.estilounico.service.impl;

import com.estilounico.model.Categoria;
import com.estilounico.repository.CategoriaRepository;
import com.estilounico.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoriaServiceImpl implements CategoriaService {
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Override
    public Categoria guardar(Categoria categoria) {
        if (categoriaRepository.existsByNombre(categoria.getNombre())) {
            throw new RuntimeException("Ya existe una categoría con el nombre: " + categoria.getNombre());
        }
        return categoriaRepository.save(categoria);
    }
    
    @Override
    public Categoria actualizar(Categoria categoria) {
        if (!categoriaRepository.existsById(categoria.getId())) {
            throw new RuntimeException("Categoría no encontrada con ID: " + categoria.getId());
        }
        
        // Verificar si el nuevo nombre ya existe en otra categoría
        Optional<Categoria> existente = categoriaRepository.findByNombre(categoria.getNombre());
        if (existente.isPresent() && !existente.get().getId().equals(categoria.getId())) {
            throw new RuntimeException("Ya existe otra categoría con el nombre: " + categoria.getNombre());
        }
        
        return categoriaRepository.save(categoria);
    }
    
    @Override
    public void eliminar(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada con ID: " + id);
        }
        // Nota: Si hay productos asociados, la BD lanzará error por RESTRICT
        categoriaRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Categoria> buscarPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Categoria> listarActivas() {
        return categoriaRepository.findByActivo(true);
    }
    
//    @Override
//    @Transactional(readOnly = true)
//    public List<Categoria> buscarPorNombre(String nombre) {
//        return categoriaRepository.findByNombreContainingIgnoreCase(nombre);
//    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existeNombre(String nombre) {
        return categoriaRepository.existsByNombre(nombre);
    }
    
    @Override
    public void activarDesactivar(Long id, Boolean activo) {
        Categoria categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
        categoria.setActivo(activo);
        categoriaRepository.save(categoria);
    }
}