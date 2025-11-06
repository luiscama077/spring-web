package com.estilounico.repository;

import com.estilounico.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    Optional<Cliente> findByUsuarioId(Long usuarioId);
    
    List<Cliente> findByClienteFrecuente(Boolean clienteFrecuente);
    
    List<Cliente> findByNombreCompletoContainingIgnoreCase(String nombre);
    
    @Query("SELECT c FROM Cliente c ORDER BY c.totalCompras DESC")
    List<Cliente> findTopClientesByCompras();
    
    @Query("SELECT c FROM Cliente c ORDER BY c.numeroPedidos DESC")
    List<Cliente> findTopClientesByPedidos();
    
    List<Cliente> findByNombreCompletoContainingOrDniContaining(String nombre, String dni);

    @Query("SELECT c FROM Cliente c LEFT JOIN FETCH c.pedidos WHERE c.id = :id")
    Optional<Cliente> findByIdWithPedidos(@Param("id") Long id);
}