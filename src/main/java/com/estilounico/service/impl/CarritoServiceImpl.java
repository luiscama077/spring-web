package com.estilounico.service.impl;

import com.estilounico.model.CarritoItem;
import com.estilounico.model.Cliente;
import com.estilounico.model.Producto;
import com.estilounico.repository.CarritoItemRepository;
import com.estilounico.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CarritoServiceImpl implements CarritoService {
    
    @Autowired
    private CarritoItemRepository carritoItemRepository;
    
    @Override
    public CarritoItem agregarAlCarrito(Cliente cliente, Producto producto, Integer cantidad) {
        // Validar cliente
        if (cliente == null || cliente.getId() == null) {
            throw new RuntimeException("Debe especificar un cliente válido");
        }
        
        // Validar producto
        if (producto == null || producto.getId() == null) {
            throw new RuntimeException("Debe especificar un producto válido");
        }
        
        // Validar cantidad positiva
        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }
        
        // Validar que el producto esté activo
        if (!producto.getActivo()) {
            throw new RuntimeException("El producto no está disponible");
        }
        
        // Validar stock disponible
        if (producto.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + producto.getStock());
        }
        
        // Verificar si el producto ya está en el carrito
        Optional<CarritoItem> existente = carritoItemRepository.findByClienteAndProducto(cliente, producto);
        
        if (existente.isPresent()) {
            // Si ya existe, actualizar la cantidad
            CarritoItem item = existente.get();
            Integer nuevaCantidad = item.getCantidad() + cantidad;
            
            // Validar stock para la nueva cantidad
            if (producto.getStock() < nuevaCantidad) {
                throw new RuntimeException("Stock insuficiente para la cantidad solicitada. Disponible: " + producto.getStock());
            }
            
            item.setCantidad(nuevaCantidad);
            return carritoItemRepository.save(item);
        } else {
            // Si no existe, crear nuevo item
            CarritoItem nuevoItem = new CarritoItem(cliente, producto, cantidad);
            return carritoItemRepository.save(nuevoItem);
        }
    }
    
    @Override
    public CarritoItem actualizarCantidad(Long carritoItemId, Integer nuevaCantidad) {
        if (nuevaCantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }
        
        CarritoItem item = carritoItemRepository.findById(carritoItemId)
            .orElseThrow(() -> new RuntimeException("Item del carrito no encontrado con ID: " + carritoItemId));
        
        // Validar stock disponible
        if (item.getProducto().getStock() < nuevaCantidad) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + item.getProducto().getStock());
        }
        
        item.setCantidad(nuevaCantidad);
        return carritoItemRepository.save(item);
    }
    
    @Override
    public void eliminarDelCarrito(Long carritoItemId) {
        if (!carritoItemRepository.existsById(carritoItemId)) {
            throw new RuntimeException("Item del carrito no encontrado con ID: " + carritoItemId);
        }
        carritoItemRepository.deleteById(carritoItemId);
    }
    
    @Override
    public void vaciarCarrito(Cliente cliente) {
        if (cliente == null || cliente.getId() == null) {
            throw new RuntimeException("Debe especificar un cliente válido");
        }
        carritoItemRepository.deleteByCliente(cliente);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<CarritoItem> buscarPorId(Long id) {
        return carritoItemRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<CarritoItem> buscarPorClienteYProducto(Cliente cliente, Producto producto) {
        return carritoItemRepository.findByClienteAndProducto(cliente, producto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CarritoItem> listarPorCliente(Cliente cliente) {
        if (cliente == null || cliente.getId() == null) {
            throw new RuntimeException("Debe especificar un cliente válido");
        }
        return carritoItemRepository.findByCliente(cliente);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long contarItemsCarrito(Cliente cliente) {
        if (cliente == null || cliente.getId() == null) {
            throw new RuntimeException("Debe especificar un cliente válido");
        }
        return carritoItemRepository.countByCliente(cliente);
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularTotalCarrito(Cliente cliente) {
        if (cliente == null || cliente.getId() == null) {
            throw new RuntimeException("Debe especificar un cliente válido");
        }
        
        List<CarritoItem> items = carritoItemRepository.findByCliente(cliente);
        
        BigDecimal total = BigDecimal.ZERO;
        for (CarritoItem item : items) {
            BigDecimal subtotal = item.getProducto().getPrecio()
                .multiply(new BigDecimal(item.getCantidad()));
            total = total.add(subtotal);
        }
        
        return total;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existeEnCarrito(Cliente cliente, Producto producto) {
        if (cliente == null || cliente.getId() == null) {
            throw new RuntimeException("Debe especificar un cliente válido");
        }
        if (producto == null || producto.getId() == null) {
            throw new RuntimeException("Debe especificar un producto válido");
        }
        return carritoItemRepository.existsByClienteAndProducto(cliente, producto);
    }
}