package com.estilounico.service;

import com.estilounico.model.CarritoItem;
import com.estilounico.model.Cliente;
import com.estilounico.model.Producto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CarritoService {
    
    CarritoItem agregarAlCarrito(Cliente cliente, Producto producto, Integer cantidad);
    
    CarritoItem actualizarCantidad(Long carritoItemId, Integer nuevaCantidad);
    
    void eliminarDelCarrito(Long carritoItemId);
    
    void vaciarCarrito(Cliente cliente);
    
    Optional<CarritoItem> buscarPorId(Long id);
    
    Optional<CarritoItem> buscarPorClienteYProducto(Cliente cliente, Producto producto);
    
    List<CarritoItem> listarPorCliente(Cliente cliente);
    
    Long contarItemsCarrito(Cliente cliente);
    
    BigDecimal calcularTotalCarrito(Cliente cliente);
    
    boolean existeEnCarrito(Cliente cliente, Producto producto);
}