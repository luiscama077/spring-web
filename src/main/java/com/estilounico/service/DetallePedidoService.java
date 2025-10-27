package com.estilounico.service;

import com.estilounico.model.DetallePedido;
import com.estilounico.model.Pedido;
import com.estilounico.model.Producto;
import java.util.List;
import java.util.Optional;

public interface DetallePedidoService {
    
    DetallePedido guardar(DetallePedido detallePedido);
    
    DetallePedido actualizar(DetallePedido detallePedido);
    
    void eliminar(Long id);
    
    Optional<DetallePedido> buscarPorId(Long id);
    
    List<DetallePedido> listarTodos();
    
    List<DetallePedido> listarPorPedido(Pedido pedido);
    
    List<DetallePedido> listarPorProducto(Producto producto);
    
    List<Object[]> obtenerProductosMasVendidos();
}