package com.estilounico.service.impl;

import com.estilounico.model.Cliente;
import com.estilounico.model.Empleado;
import com.estilounico.model.Pedido;
import com.estilounico.model.enums.EstadoPedido;
import com.estilounico.repository.EmpleadoRepository;
import com.estilounico.repository.PedidoRepository;
import com.estilounico.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private EmpleadoRepository empleadoRepository;
    
    @Override
    public Pedido guardar(Pedido pedido) {
        // Validar que el cliente no sea null
        if (pedido.getCliente() == null || pedido.getCliente().getId() == null) {
            throw new RuntimeException("Debe especificar un cliente válido");
        }
        
        // Validar que el total sea positivo
        if (pedido.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El total del pedido debe ser mayor a cero");
        }
        
        // Validar que tenga detalles
        if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            throw new RuntimeException("El pedido debe tener al menos un producto");
        }
        
        return pedidoRepository.save(pedido);
    }
    
    @Override
    public Pedido actualizar(Pedido pedido) {
        if (!pedidoRepository.existsById(pedido.getId())) {
            throw new RuntimeException("Pedido no encontrado con ID: " + pedido.getId());
        }
        return pedidoRepository.save(pedido);
    }
    
    @Override
    public void eliminar(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new RuntimeException("Pedido no encontrado con ID: " + id);
        }
        pedidoRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Pedido> listarTodosOrdenados() {
        return pedidoRepository.findAllOrderByFechaPedidoDesc();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Pedido> listarPorCliente(Cliente cliente) {
        return pedidoRepository.findByCliente(cliente);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Pedido> listarPorClienteOrdenados(Cliente cliente) {
        return pedidoRepository.findByClienteOrderByFechaPedidoDesc(cliente);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Pedido> listarPorEstado(EstadoPedido estado) {
        return pedidoRepository.findByEstado(estado);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Pedido> listarPorEstadoOrdenados(EstadoPedido estado) {
        return pedidoRepository.findByEstadoOrderByFechaPedidoDesc(estado);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Pedido> listarPorEmpleado(Empleado empleado) {
        return pedidoRepository.findByEmpleadoProcesador(empleado);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Pedido> listarPorPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        if (fechaInicio.isAfter(fechaFin)) {
            throw new RuntimeException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        return pedidoRepository.findByFechaPedidoBetween(fechaInicio, fechaFin);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long contarPorEstado(EstadoPedido estado) {
        return pedidoRepository.countByEstado(estado);
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularTotalVentas() {
        BigDecimal total = pedidoRepository.calcularTotalVentas();
        return total != null ? total : BigDecimal.ZERO;
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularVentasPorPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        if (fechaInicio.isAfter(fechaFin)) {
            throw new RuntimeException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        BigDecimal total = pedidoRepository.calcularVentasPorPeriodo(fechaInicio, fechaFin);
        return total != null ? total : BigDecimal.ZERO;
    }
    
    @Override
    public void cambiarEstado(Long id, EstadoPedido nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
        
        // Validar transiciones de estado válidas
        EstadoPedido estadoActual = pedido.getEstado();
        
        if (estadoActual == EstadoPedido.CANCELADO) {
            throw new RuntimeException("No se puede cambiar el estado de un pedido cancelado");
        }
        
        if (estadoActual == EstadoPedido.ENTREGADO && nuevoEstado != EstadoPedido.CANCELADO) {
            throw new RuntimeException("No se puede cambiar el estado de un pedido ya entregado");
        }
        
        pedido.setEstado(nuevoEstado);
        pedidoRepository.save(pedido);
    }
    
    @Override
    public void asignarEmpleado(Long pedidoId, Long empleadoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + pedidoId));
        
        Empleado empleado = empleadoRepository.findById(empleadoId)
            .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + empleadoId));
        
        pedido.setEmpleadoProcesador(empleado);
        pedidoRepository.save(pedido);
    }
}