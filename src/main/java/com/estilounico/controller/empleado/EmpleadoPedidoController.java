package com.estilounico.controller.empleado;

import com.estilounico.model.Cliente;
import com.estilounico.model.DetallePedido;
import com.estilounico.model.Pedido;
import com.estilounico.model.Producto;
import com.estilounico.model.enums.EstadoPedido;
import com.estilounico.service.ClienteService;
import com.estilounico.service.PedidoService;
import com.estilounico.service.ProductoService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/empleado/pedidos")
public class EmpleadoPedidoController {

    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private ProductoService productoService;

    @GetMapping
    public String listarPedidos(@RequestParam(required = false) String estado, Model model) {
        
        List<Pedido> pedidos;
        
        if (estado != null && !estado.isEmpty()) {
            try {
                // Convertir el string del parámetro a un valor del Enum
                EstadoPedido estadoEnum = EstadoPedido.valueOf(estado.toUpperCase());
                pedidos = pedidoService.listarPorEstado(estadoEnum);
                model.addAttribute("estadoSeleccionado", estadoEnum.name());
            } catch (IllegalArgumentException e) {
                // Si el estado no es válido, se muestran todos los pedidos
                pedidos = pedidoService.listarTodos();
            }
        } else {
            // Si no hay parámetro de estado, se muestran todos
            pedidos = pedidoService.listarTodos();
        }

        model.addAttribute("pedidos", pedidos);
        model.addAttribute("estados", EstadoPedido.values()); 
        
        return "empleado/pedidos-e";
    }
    
    @GetMapping("/detalle/{id}")
    public String verDetallePedido(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Pedido pedido = pedidoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
            
            model.addAttribute("pedido", pedido);
            model.addAttribute("estadosPosibles", EstadoPedido.values());
            
            return "empleado/pedido-detalle-e";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cargar el pedido: " + e.getMessage());
            return "redirect:/empleado/pedidos";
        }
    }

    @PostMapping("/actualizar-estado")
    public String actualizarEstado(@RequestParam Long pedidoId,
                                   @RequestParam EstadoPedido nuevoEstado,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        try {
            // Obtener el ID del empleado logueado desde la sesión
            Long empleadoId = (Long) session.getAttribute("empleadoId");
            if (empleadoId == null) {
                throw new RuntimeException("No se pudo identificar al empleado en la sesión.");
            }
            
            // Obtenemos el pedido completo con sus detalles y cliente
            Pedido pedido = pedidoService.buscarPorIdConDetalles(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

            EstadoPedido estadoAnterior = pedido.getEstado();
            pedidoService.actualizarEstadoYAsignarEmpleado(pedidoId, nuevoEstado, empleadoId);
            

            // Si el nuevo estado es CANCELADO y el estado anterior NO era CANCELADO
            // (para evitar restar múltiples veces si se cancela y se vuelve a cancelar)
            if (nuevoEstado == EstadoPedido.CANCELADO && estadoAnterior != EstadoPedido.CANCELADO) {
                
                Cliente cliente = pedido.getCliente();
                
                // Restamos el total de la compra
                BigDecimal nuevoTotalCompras = cliente.getTotalCompras().subtract(pedido.getTotal());
                cliente.setTotalCompras(nuevoTotalCompras.max(BigDecimal.ZERO)); // Aseguramos que no sea negativo

                // Decrementamos el número de pedidos
                cliente.setNumeroPedidos(Math.max(0, cliente.getNumeroPedidos() - 1)); // Aseguramos que no sea negativo

                // Opcional: Re-evaluar si sigue siendo cliente frecuente
                if (cliente.getNumeroPedidos() <= 1) {
                    cliente.setClienteFrecuente(false);
                }
                
                clienteService.actualizar(cliente);
                
                // Devolver el stock de los productos del pedido cancelado
                for (DetallePedido detalle : pedido.getDetalles()) {
                    Producto producto = detalle.getProducto();
                    producto.setStock(producto.getStock() + detalle.getCantidad());
                    productoService.guardar(producto);
                }
            }
           
            redirectAttributes.addFlashAttribute("mensaje", "Estado del pedido #" + pedidoId + " actualizado a " + nuevoEstado);
            redirectAttributes.addFlashAttribute("tipo", "success");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al actualizar el estado: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        
        return "redirect:/empleado/pedidos/detalle/" + pedidoId;
    }
}
