package com.estilounico.repository;

import com.estilounico.model.Categoria;
import com.estilounico.model.Producto;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>, JpaSpecificationExecutor<Producto>{ // Specification para rol clientes
    
    List<Producto> findByActivo(Boolean activo);
    
    List<Producto> findByCategoria(Categoria categoria);
        
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    
    @Query("SELECT p FROM Producto p WHERE p.stock < :cantidad AND p.activo = true")
    List<Producto> findProductosConBajoStock(@Param("cantidad") Integer cantidad);
    
    long countByStockLessThan(int umbral);
    
    List<Producto> findByNombreContainingAndActivoTrueAndStockGreaterThan(String nombre, int stock);

    @Query("SELECT p FROM Producto p JOIN DetallePedido dp ON p.id = dp.producto.id " +
            "GROUP BY p.id " +
            "ORDER BY SUM(dp.cantidad) DESC")
    List<Producto> findMasVendidos(Pageable pageable);
    
}