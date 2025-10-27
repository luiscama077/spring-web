package com.estilounico.repository;

import com.estilounico.model.CarritoItem;
import com.estilounico.model.Cliente;
import com.estilounico.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {
    
    List<CarritoItem> findByCliente(Cliente cliente);
    
    Optional<CarritoItem> findByClienteAndProducto(Cliente cliente, Producto producto);
    
    boolean existsByClienteAndProducto(Cliente cliente, Producto producto);
    
    @Modifying
    @Query("DELETE FROM CarritoItem ci WHERE ci.cliente = :cliente")
    void deleteByCliente(@Param("cliente") Cliente cliente);
    
    @Query("SELECT COUNT(ci) FROM CarritoItem ci WHERE ci.cliente = :cliente")
    Long countByCliente(@Param("cliente") Cliente cliente);
}