package com.estilounico.service;

import com.estilounico.model.Cliente;
import com.estilounico.model.Empleado;
import com.estilounico.model.Pedido;
import com.estilounico.model.enums.EstadoPedido;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PedidoService {
    
    Pedido guardar(Pedido pedido);
    
    Pedido actualizar(Pedido pedido);
    
    void eliminar(Long id);
    
    Optional<Pedido> buscarPorId(Long id);
    
    List<Pedido> listarTodos();
    
    List<Pedido> listarTodosOrdenados();
    
    List<Pedido> listarPorCliente(Cliente cliente);
    
    List<Pedido> listarPorClienteOrdenados(Cliente cliente);
    
    List<Pedido> listarPorEstado(EstadoPedido estado);
    
    List<Pedido> listarPorEstadoOrdenados(EstadoPedido estado);
    
    List<Pedido> listarPorEmpleado(Empleado empleado);
    
    List<Pedido> listarPorPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    Long contarPorEstado(EstadoPedido estado);
    
    BigDecimal calcularTotalVentas();
    
    BigDecimal calcularVentasPorPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    void cambiarEstado(Long id, EstadoPedido nuevoEstado);
    
    void asignarEmpleado(Long pedidoId, Long empleadoId);
}