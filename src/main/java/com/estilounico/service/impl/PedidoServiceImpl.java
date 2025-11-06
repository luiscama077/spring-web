package com.estilounico.service.impl;

import com.estilounico.model.CarritoItem;
import com.estilounico.model.Cliente;
import com.estilounico.model.DetallePedido;
import com.estilounico.model.Empleado;
import com.estilounico.model.Pedido;
import com.estilounico.model.Producto;
import com.estilounico.model.enums.EstadoPedido;
import com.estilounico.repository.CarritoItemRepository;
import com.estilounico.repository.ClienteRepository;
import com.estilounico.repository.EmpleadoRepository;
import com.estilounico.repository.PedidoRepository;
import com.estilounico.repository.ProductoRepository;
import com.estilounico.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private EmpleadoRepository empleadoRepository;
    
    @Autowired 
    private ProductoRepository productoRepository;
    
    @Autowired 
    private ClienteRepository clienteRepository;
    
    @Autowired
    private CarritoItemRepository carritoItemRepository;

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
    	return pedidoRepository.findByIdWithDetalles(id);    }
    
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
    public List<Pedido> listarUltimosPedidos(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("fechaPedido").descending());
        return pedidoRepository.findAll(pageable).getContent();
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
    
    @Override
    public void actualizarEstadoYAsignarEmpleado(Long pedidoId, EstadoPedido nuevoEstado, Long empleadoId) {
        // 1. Buscar el pedido
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + pedidoId));

        // 2. Buscar el empleado
        Empleado empleado = empleadoRepository.findById(empleadoId)
            .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + empleadoId));

        // 3. Actualizar el estado del pedido
        pedido.setEstado(nuevoEstado);
        pedido.setEmpleadoProcesador(empleado);
        
        // 5. Guardar los cambios en la base de datos
        pedidoRepository.save(pedido);
    }
    
    @Override
    @Transactional
    public Long crearPedidoEnTienda(Long clienteId, List<Long> productoIds, List<Integer> cantidades, Long empleadoId) {
        
        // 1. Obtener el cliente
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clienteId));

        // 2. Crear la cabecera del pedido
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setEstado(EstadoPedido.ENTREGADO);
        pedido.setDireccionEnvio("Venta en tienda");
        
        BigDecimal totalPedido = BigDecimal.ZERO;
        List<DetallePedido> detalles = new ArrayList<>();

        // 3. Procesar cada línea de la venta
        for (int i = 0; i < productoIds.size(); i++) {
            Long productoId = productoIds.get(i);
            int cantidad = cantidades.get(i);

            // 3.1. Obtener el producto y validar stock
            Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: ID " + productoId));

            if (producto.getStock() < cantidad) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
            }

            // 3.2. Crear el detalle usando el nuevo constructor
            DetallePedido nuevoDetalle = new DetallePedido(pedido, producto, cantidad);
            
            detalles.add(nuevoDetalle);
            totalPedido = totalPedido.add(nuevoDetalle.getSubtotal());

            // 3.3. Actualizar el stock del producto
            producto.setStock(producto.getStock() - cantidad);
            // No es necesario llamar a save() aquí, @Transactional se encargará al final.
        }

        Empleado empleado = empleadoRepository.findById(empleadoId)
        		.orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + empleadoId));
        // 4. Asignar el total y la lista de detalles al pedido
        pedido.setTotal(totalPedido);
        pedido.setDetalles(detalles);
        pedido.setEmpleadoProcesador(empleado);
        
        // 5. Guardar el pedido completo (JPA guardará los detalles en cascada)
        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        
        cliente.setNumeroPedidos(cliente.getNumeroPedidos() + 1);
        cliente.setTotalCompras(cliente.getTotalCompras().add(pedido.getTotal())); 
        
        // Opcional: Actualizar estado de cliente frecuente
        if (cliente.getNumeroPedidos() > 3) {
            cliente.setClienteFrecuente(true);
        }
        
        clienteRepository.save(cliente);
        return pedidoGuardado.getId();
    }
    
    @Override
    @Transactional
    public void crearPedidoDesdeCarrito(Long clienteId, String direccionEnvio) {
        // 1. Obtener los items del carrito del cliente
        List<CarritoItem> carritoItems = carritoItemRepository.findByClienteId(clienteId);
        if (carritoItems.isEmpty()) {
            throw new IllegalStateException("El carrito está vacío. No se puede crear un pedido.");
        }

        // 2. Obtener la entidad Cliente
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // 3. Crear la cabecera del Pedido
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setDireccionEnvio(direccionEnvio);
        pedido.setEstado(EstadoPedido.PENDIENTE); // Los pedidos online empiezan como pendientes

        BigDecimal totalPedido = BigDecimal.ZERO;
        List<DetallePedido> detalles = new ArrayList<>();

        // 4. Convertir cada CarritoItem en un DetallePedido
        for (CarritoItem item : carritoItems) {
            Producto producto = item.getProducto();
            
            // Validar stock antes de procesar
            if (producto.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }
            
            DetallePedido detalle = new DetallePedido(pedido, producto, item.getCantidad());
            detalles.add(detalle);
            totalPedido = totalPedido.add(detalle.getSubtotal());
            
            // 5. Actualizar el stock del producto
            producto.setStock(producto.getStock() - item.getCantidad());
        }

        pedido.setTotal(totalPedido);
        pedido.setDetalles(detalles);

        // 6. Guardar el pedido (los detalles y las actualizaciones de stock se guardan en cascada gracias a @Transactional)
        pedidoRepository.save(pedido);
        
        cliente.setNumeroPedidos(cliente.getNumeroPedidos() + 1);
        cliente.setTotalCompras(cliente.getTotalCompras().add(pedido.getTotal())); 
        
        if (cliente.getNumeroPedidos() > 3) {
            cliente.setClienteFrecuente(true);
        }
        
        clienteRepository.save(cliente);

        // 7. Limpiar el carrito del cliente
        carritoItemRepository.deleteAll(carritoItems);
    }
    
    @Override
    public List<Pedido> listarPorCliente(Long clienteId) {
        // Ordenamos por fecha para mostrar los más recientes primero
        return pedidoRepository.findByClienteIdOrderByFechaPedidoDesc(clienteId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Pedido> buscarPorIdConDetalles(Long id) {
        return pedidoRepository.findByIdWithDetalles(id);
    }
    
    
}