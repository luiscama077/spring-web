package com.estilounico.service;

import com.estilounico.model.CarritoItem;
import java.util.List;

public interface CarritoService {
    
    List<CarritoItem> listarItemsPorCliente(Long clienteId);
    
    void agregarProducto(Long clienteId, Long productoId, int cantidad);
    
    void eliminarItem(Long itemId, Long clienteId);
    
    void actualizarCantidad(Long itemId, int cantidad, Long clienteId);

}