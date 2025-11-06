package com.estilounico.controller.cliente;

import com.estilounico.model.CarritoItem;
import com.estilounico.service.CarritoService;
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
@RequestMapping("/cliente/carrito" )
public class ClienteCarritoController {

    @Autowired
    private CarritoService carritoService;

    // Método para mostrar el contenido del carrito
    @GetMapping
    public String mostrarCarrito(HttpSession session, Model model) {
        Long clienteId = (Long) session.getAttribute("clienteId");
        
        List<CarritoItem> items = carritoService.listarItemsPorCliente(clienteId);
        model.addAttribute("carritoItems", items);
        
        double total = items.stream()
                            .mapToDouble(item -> item.getProducto().getPrecio().doubleValue() * item.getCantidad())
                            .sum();
        model.addAttribute("totalCarrito", total);
        
        return "cliente/carrito-c";
    }

    // Método para agregar un producto al carrito
    @PostMapping("/agregar")
    public String agregarAlCarrito(@RequestParam Long productoId,
                                   @RequestParam(defaultValue = "1") int cantidad,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        try {
            Long clienteId = (Long) session.getAttribute("clienteId");
            if (clienteId == null) {
                return "redirect:/login";
            }

            carritoService.agregarProducto(clienteId, productoId, cantidad);
            
            redirectAttributes.addFlashAttribute("mensaje", "¡Producto añadido al carrito!");
            redirectAttributes.addFlashAttribute("tipo", "success");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al añadir el producto: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        
        return "redirect:/cliente/productos/detalle/" + productoId;
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarDelCarrito(@PathVariable Long id,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        try {
            Long clienteId = (Long) session.getAttribute("clienteId");
            if (clienteId == null) {
                return "redirect:/login";
            }

            // Llamamos al servicio para eliminar el item
            carritoService.eliminarItem(id, clienteId);
            
            redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado del carrito.");
            redirectAttributes.addFlashAttribute("tipo", "info"); 

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar el producto: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        
        return "redirect:/cliente/carrito";
    }
    
    @PostMapping("/actualizar")
    public String actualizarCantidad(@RequestParam Long itemId,
                                     @RequestParam int cantidad,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        try {
            Long clienteId = (Long) session.getAttribute("clienteId");
            if (clienteId == null) {
                return "redirect:/login";
            }

            carritoService.actualizarCantidad(itemId, cantidad, clienteId);
            
            redirectAttributes.addFlashAttribute("mensaje", "Cantidad actualizada correctamente.");
            redirectAttributes.addFlashAttribute("tipo", "success");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al actualizar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        
        return "redirect:/cliente/carrito";
    }
}
