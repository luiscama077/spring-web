package com.estilounico.controller.cliente;

import com.estilounico.model.CarritoItem;
import com.estilounico.model.Cliente;
import com.estilounico.model.Pedido;
import com.estilounico.service.CarritoService;
import com.estilounico.service.ClienteService;
import com.estilounico.service.PedidoService;
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

import java.util.List;

@Controller
@RequestMapping("/cliente" )
public class ClientePedidoController {

    @Autowired
    private CarritoService carritoService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private PedidoService pedidoService;

    // Muestra la página de checkout con el resumen
    @GetMapping("/checkout")
    public String mostrarCheckout(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Long clienteId = (Long) session.getAttribute("clienteId");
        
        List<CarritoItem> items = carritoService.listarItemsPorCliente(clienteId);
        
        // Si el carrito está vacío, Redirigir.
        if (items.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "Tu carrito está vacío. Añade productos para poder comprar.");
            redirectAttributes.addFlashAttribute("tipo", "info");
            return "redirect:/cliente/productos";
        }
        
        Cliente cliente = clienteService.buscarPorId(clienteId).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        model.addAttribute("carritoItems", items);
        model.addAttribute("cliente", cliente);
        
        double total = items.stream()
                            .mapToDouble(item -> item.getProducto().getPrecio().doubleValue() * item.getCantidad())
                            .sum();
        model.addAttribute("totalCarrito", total);
        
        return "cliente/checkout-c";
    }

    // Procesa la creación del pedido
    @PostMapping("/checkout/procesar")
    public String procesarPedido(@RequestParam String direccionEnvio,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        try {
            Long clienteId = (Long) session.getAttribute("clienteId");
            
            pedidoService.crearPedidoDesdeCarrito(clienteId, direccionEnvio);
            
            redirectAttributes.addFlashAttribute("mensaje", "¡Gracias por tu compra! Tu pedido ha sido registrado.");
            redirectAttributes.addFlashAttribute("tipo", "success");
            
            return "redirect:/cliente/mis-pedidos";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al procesar tu pedido: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
            return "redirect:/cliente/carrito";
        }
    }
    
    @GetMapping("/mis-pedidos")
    public String mostrarMisPedidos(HttpSession session, Model model) {
        Long clienteId = (Long) session.getAttribute("clienteId");
        
        List<Pedido> pedidos = pedidoService.listarPorCliente(clienteId);
        model.addAttribute("pedidos", pedidos);
        
        return "cliente/mis-pedidos-c";
    }
    
    @GetMapping("/pedidos/detalle/{id}")
    public String mostrarDetallePedido(@PathVariable Long id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        try {
            Long clienteId = (Long) session.getAttribute("clienteId");
            
            Pedido pedido = pedidoService.buscarPorIdConDetalles(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado."));

            // Verificación de seguridad: el pedido debe pertenecer al cliente logueado
            if (!pedido.getCliente().getId().equals(clienteId)) {
                throw new SecurityException("Acceso denegado.");
            }

            model.addAttribute("pedido", pedido);
            return "cliente/pedido-detalle-c";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al ver el detalle del pedido: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
            return "redirect:/cliente/mis-pedidos";
        }
    }
}
