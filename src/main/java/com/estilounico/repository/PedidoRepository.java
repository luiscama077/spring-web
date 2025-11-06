package com.estilounico.repository;

import com.estilounico.model.Cliente;
import com.estilounico.model.Empleado;
import com.estilounico.model.Pedido;
import com.estilounico.model.enums.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    List<Pedido> findByCliente(Cliente cliente);
    
    List<Pedido> findByClienteOrderByFechaPedidoDesc(Cliente cliente);
    
    List<Pedido> findByEstado(EstadoPedido estado);
    
    List<Pedido> findByEstadoOrderByFechaPedidoDesc(EstadoPedido estado);
    
    List<Pedido> findByEmpleadoProcesador(Empleado empleado);
    
    @Query("SELECT p FROM Pedido p ORDER BY p.fechaPedido DESC")
    List<Pedido> findAllOrderByFechaPedidoDesc();
    
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.estado = :estado")
    Long countByEstado(@Param("estado") EstadoPedido estado);
    
    @Query("SELECT SUM(p.total) FROM Pedido p WHERE p.estado != 'CANCELADO'")
    BigDecimal calcularTotalVentas();
    
    @Query("SELECT p FROM Pedido p JOIN FETCH p.detalles WHERE p.id = :id")
    Optional<Pedido> findByIdWithDetalles(@Param("id") Long id);
    
    List<Pedido> findByClienteIdOrderByFechaPedidoDesc(Long clienteId);
    

}