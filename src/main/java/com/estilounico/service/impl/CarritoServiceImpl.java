package com.estilounico.service.impl;

import com.estilounico.model.CarritoItem;
import com.estilounico.model.Cliente;
import com.estilounico.model.Producto;
import com.estilounico.repository.CarritoItemRepository;
import com.estilounico.repository.ClienteRepository;
import com.estilounico.repository.ProductoRepository;
import com.estilounico.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class CarritoServiceImpl implements CarritoService {
    
    @Autowired
    private CarritoItemRepository carritoItemRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<CarritoItem> listarItemsPorCliente(Long clienteId) {
        return carritoItemRepository.findByClienteId(clienteId);
    }

    @Override
    public void agregarProducto(Long clienteId, Long productoId, int cantidad) {
        // Buscar si el item ya existe para este cliente y producto
        CarritoItem itemExistente = carritoItemRepository.findByClienteIdAndProductoId(clienteId, productoId);

        if (itemExistente != null) {
            // Si existe, actualizamos la cantidad
            int nuevaCantidad = itemExistente.getCantidad() + cantidad;
            itemExistente.setCantidad(nuevaCantidad);
            carritoItemRepository.save(itemExistente);
        } else {
            // Si no existe, creamos un nuevo item
            Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            Producto producto = productoRepository.findById(productoId).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            
            CarritoItem nuevoItem = new CarritoItem();
            nuevoItem.setCliente(cliente);
            nuevoItem.setProducto(producto);
            nuevoItem.setCantidad(cantidad);
            carritoItemRepository.save(nuevoItem);
        }
    }
    
    @Override
    public void eliminarItem(Long itemId, Long clienteId) {
        // 1. Buscamos el item del carrito por su ID
        CarritoItem item = carritoItemRepository.findById(itemId)
            .orElseThrow(() -> new RuntimeException("Item del carrito no encontrado."));

        // 2. Verificación de seguridad: nos aseguramos de que el item pertenezca al cliente logueado.
        if (!item.getCliente().getId().equals(clienteId)) {
            // Si no coincide, lanzamos una excepción para evitar que un usuario borre el carrito de otro.
            throw new SecurityException("Acceso denegado: no puedes eliminar un item que no te pertenece.");
        }

        // 3. Si todo es correcto, eliminamos el item.
        carritoItemRepository.delete(item);
    }
    
    @Override
    public void actualizarCantidad(Long itemId, int cantidad, Long clienteId) {
        // 1. Buscamos el item del carrito
        CarritoItem item = carritoItemRepository.findById(itemId)
            .orElseThrow(() -> new RuntimeException("Item del carrito no encontrado."));

        // 2. Verificación de seguridad (el item debe pertenecer al cliente)
        if (!item.getCliente().getId().equals(clienteId)) {
            throw new SecurityException("Acceso denegado.");
        }

        // 3. Si la cantidad es 0 o menos, lo eliminamos
        if (cantidad <= 0) {
            carritoItemRepository.delete(item);
            return; // Terminamos la ejecución
        }

        // 4. Verificación de stock
        Producto producto = item.getProducto();
        if (producto.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente. Solo quedan " + producto.getStock() + " unidades de '" + producto.getNombre() + "'.");
        }

        // 5. Si todo es correcto, actualizamos la cantidad y guardamos
        item.setCantidad(cantidad);
        carritoItemRepository.save(item);
    }
}