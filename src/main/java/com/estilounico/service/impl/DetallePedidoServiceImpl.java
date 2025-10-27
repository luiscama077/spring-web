package com.estilounico.service.impl;

import com.estilounico.model.DetallePedido;
import com.estilounico.model.Pedido;
import com.estilounico.model.Producto;
import com.estilounico.repository.DetallePedidoRepository;
import com.estilounico.service.DetallePedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DetallePedidoServiceImpl implements DetallePedidoService {
    
    @Autowired
    private DetallePedidoRepository detallePedidoRepository;
    
    @Override
    public DetallePedido guardar(DetallePedido detallePedido) {
        // Validar que el pedido no sea null
        if (detallePedido.getPedido() == null || detallePedido.getPedido().getId() == null) {
            throw new RuntimeException("Debe especificar un pedido válido");
        }
        
        // Validar que el producto no sea null
        if (detallePedido.getProducto() == null || detallePedido.getProducto().getId() == null) {
            throw new RuntimeException("Debe especificar un producto válido");
        }
        
        // Validar cantidad positiva
        if (detallePedido.getCantidad() <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }
        
        // Validar precio unitario positivo
        if (detallePedido.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El precio unitario debe ser mayor a cero");
        }
        
        // Calcular subtotal automáticamente
        detallePedido.calcularSubtotal();
        
        return detallePedidoRepository.save(detallePedido);
    }
    
    @Override
    public DetallePedido actualizar(DetallePedido detallePedido) {
        if (!detallePedidoRepository.existsById(detallePedido.getId())) {
            throw new RuntimeException("Detalle de pedido no encontrado con ID: " + detallePedido.getId());
        }
        
        // Recalcular subtotal
        detallePedido.calcularSubtotal();
        
        return detallePedidoRepository.save(detallePedido);
    }
    
    @Override
    public void eliminar(Long id) {
        if (!detallePedidoRepository.existsById(id)) {
            throw new RuntimeException("Detalle de pedido no encontrado con ID: " + id);
        }
        detallePedidoRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<DetallePedido> buscarPorId(Long id) {
        return detallePedidoRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DetallePedido> listarTodos() {
        return detallePedidoRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DetallePedido> listarPorPedido(Pedido pedido) {
        return detallePedidoRepository.findByPedido(pedido);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DetallePedido> listarPorProducto(Producto producto) {
        return detallePedidoRepository.findByProducto(producto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Object[]> obtenerProductosMasVendidos() {
        return detallePedidoRepository.findProductosMasVendidos();
    }
}