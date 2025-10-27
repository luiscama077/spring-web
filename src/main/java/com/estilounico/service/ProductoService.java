package com.estilounico.service;

import com.estilounico.model.Categoria;
import com.estilounico.model.Producto;
import com.estilounico.model.enums.GeneroProducto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductoService {
    
    Producto guardar(Producto producto);
    
    Producto actualizar(Producto producto);
    
    void eliminar(Long id);
    
    Optional<Producto> buscarPorId(Long id);
    
    List<Producto> listarTodos();
    
    List<Producto> listarActivos();
    
    List<Producto> listarPorCategoria(Categoria categoria);
    
    List<Producto> listarPorCategoriaActivos(Categoria categoria);
    
    List<Producto> listarPorGenero(GeneroProducto genero);
    
    List<Producto> listarPorGeneroActivos(GeneroProducto genero);
    
    List<Producto> buscarPorNombre(String nombre);
    
    List<Producto> buscarPorNombreActivos(String nombre);
    
    List<Producto> listarPorMarca(String marca);
    
    List<Producto> listarPorRangoPrecio(BigDecimal precioMin, BigDecimal precioMax);
    
    List<Producto> listarProductosConBajoStock(Integer cantidad);
    
    List<Producto> listarProductosRecientes();
    
    void activarDesactivar(Long id, Boolean activo);
    
    void actualizarStock(Long id, Integer nuevoStock);
    
    void reducirStock(Long id, Integer cantidad);
    
    void aumentarStock(Long id, Integer cantidad);
}