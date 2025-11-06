package com.estilounico.service;

import com.estilounico.model.Categoria;
import com.estilounico.model.Producto;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductoService {
    
    Producto guardar(Producto producto);
    
    Producto actualizar(Producto producto);
    
    void eliminar(Long id);
    
    Optional<Producto> buscarPorId(Long id);
    
    List<Producto> listarTodos();
    
    List<Producto> listarActivos();
    
    List<Producto> listarPorCategoria(Categoria categoria);
    
    List<Producto> buscarPorNombre(String nombre);
    
    List<Producto> listarProductosConBajoStock(Integer cantidad);
    
    long contarProductosConBajoStock(int umbral);
    
    void activarDesactivar(Long id, Boolean activo);
    
    void actualizarStock(Long id, Integer nuevoStock);
    
    List<Producto> buscarActivosConStockPorNombre(String termino);

    List<Producto> listarUltimosProductos(int limit);
    
    List<Producto> listarMasVendidos(int limit);
    
    Page<Producto> listarConFiltros(Long categoriaId, String genero, String marca, Pageable pageable);

}