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
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    List<Pedido> findByCliente(Cliente cliente);
    
    List<Pedido> findByClienteOrderByFechaPedidoDesc(Cliente cliente);
    
    List<Pedido> findByEstado(EstadoPedido estado);
    
    List<Pedido> findByEstadoOrderByFechaPedidoDesc(EstadoPedido estado);
    
    List<Pedido> findByEmpleadoProcesador(Empleado empleado);
    
    List<Pedido> findByFechaPedidoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    @Query("SELECT p FROM Pedido p ORDER BY p.fechaPedido DESC")
    List<Pedido> findAllOrderByFechaPedidoDesc();
    
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.estado = :estado")
    Long countByEstado(@Param("estado") EstadoPedido estado);
    
    @Query("SELECT SUM(p.total) FROM Pedido p WHERE p.estado != 'CANCELADO'")
    BigDecimal calcularTotalVentas();
    
    @Query("SELECT SUM(p.total) FROM Pedido p WHERE p.estado != 'CANCELADO' AND p.fechaPedido BETWEEN :fechaInicio AND :fechaFin")
    BigDecimal calcularVentasPorPeriodo(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                        @Param("fechaFin") LocalDateTime fechaFin);
}