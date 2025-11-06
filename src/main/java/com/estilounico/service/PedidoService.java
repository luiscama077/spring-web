package com.estilounico.service;

import com.estilounico.model.Cliente;
import com.estilounico.model.Empleado;
import com.estilounico.model.Pedido;
import com.estilounico.model.enums.EstadoPedido;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PedidoService {
    
    void eliminar(Long id);
    
    Optional<Pedido> buscarPorId(Long id);
    
    List<Pedido> listarTodos(); //e-p
    
    List<Pedido> listarTodosOrdenados();
    
    List<Pedido> listarPorCliente(Cliente cliente);
    
    List<Pedido> listarPorClienteOrdenados(Cliente cliente);
    
    List<Pedido> listarPorEstado(EstadoPedido estado);
    
    List<Pedido> listarPorEstadoOrdenados(EstadoPedido estado);
    
    List<Pedido> listarPorEmpleado(Empleado empleado);
    
    List<Pedido> listarUltimosPedidos(int limit);
    
    Long contarPorEstado(EstadoPedido estado);
    
    BigDecimal calcularTotalVentas();
    
    void cambiarEstado(Long id, EstadoPedido nuevoEstado);
    
    void asignarEmpleado(Long pedidoId, Long empleadoId);
    
    void actualizarEstadoYAsignarEmpleado(Long pedidoId, EstadoPedido nuevoEstado, Long empleadoId);

    Long crearPedidoEnTienda(Long clienteId, List<Long> productoIds, List<Integer> cantidades, Long empleadoId);

    void crearPedidoDesdeCarrito(Long clienteId, String direccionEnvio);
    
    List<Pedido> listarPorCliente(Long clienteId);

    Optional<Pedido> buscarPorIdConDetalles(Long id);

}