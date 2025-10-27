package com.estilounico.repository;

import com.estilounico.model.Categoria;
import com.estilounico.model.Producto;
import com.estilounico.model.enums.GeneroProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    List<Producto> findByActivo(Boolean activo);
    
    List<Producto> findByCategoria(Categoria categoria);
    
    List<Producto> findByCategoriaAndActivo(Categoria categoria, Boolean activo);
    
    List<Producto> findByGenero(GeneroProducto genero);
    
    List<Producto> findByGeneroAndActivo(GeneroProducto genero, Boolean activo);
    
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    
    List<Producto> findByNombreContainingIgnoreCaseAndActivo(String nombre, Boolean activo);
    
    List<Producto> findByMarca(String marca);
    
    List<Producto> findByMarcaAndActivo(String marca, Boolean activo);
    
    List<Producto> findByPrecioBetween(BigDecimal precioMin, BigDecimal precioMax);
    
    List<Producto> findByPrecioBetweenAndActivo(BigDecimal precioMin, BigDecimal precioMax, Boolean activo);
    
    @Query("SELECT p FROM Producto p WHERE p.stock < :cantidad AND p.activo = true")
    List<Producto> findProductosConBajoStock(@Param("cantidad") Integer cantidad);
    
    @Query("SELECT p FROM Producto p WHERE p.activo = true ORDER BY p.fechaCreacion DESC")
    List<Producto> findProductosRecientes();
}