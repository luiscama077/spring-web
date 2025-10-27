package com.estilounico.controller.admin;

import com.estilounico.model.enums.EstadoPedido;
import com.estilounico.service.DetallePedidoService;
import com.estilounico.service.EmpleadoService;
import com.estilounico.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/pedidos")
public class AdminPedidoController {
    
    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private DetallePedidoService detallePedidoService;
    
    @Autowired
    private EmpleadoService empleadoService;
    
    // Listar todos los pedidos
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pedidos", pedidoService.listarTodosOrdenados());
        model.addAttribute("estados", EstadoPedido.values());
        return "admin/pedidos";
    }
    
    // Ver detalle de un pedido
    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            var pedido = pedidoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
            model.addAttribute("pedido", pedido);
            model.addAttribute("detalles", detallePedidoService.listarPorPedido(pedido));
            model.addAttribute("estados", EstadoPedido.values());
            model.addAttribute("empleados", empleadoService.listarTodos());
            return "admin/pedido-detalle";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
            return "redirect:/admin/pedidos";
        }
    }
    
    // Filtrar por estado
    @GetMapping("/filtrar")
    public String filtrar(@RequestParam EstadoPedido estado, Model model) {
        model.addAttribute("pedidos", pedidoService.listarPorEstadoOrdenados(estado));
        model.addAttribute("estadoFiltro", estado);
        model.addAttribute("estados", EstadoPedido.values());
        return "admin/pedidos";
    }
    
    // Cambiar estado de pedido
    @PostMapping("/cambiar-estado/{id}")
    public String cambiarEstado(@PathVariable Long id,
                               @RequestParam EstadoPedido nuevoEstado,
                               RedirectAttributes redirectAttributes) {
        try {
            pedidoService.cambiarEstado(id, nuevoEstado);
            redirectAttributes.addFlashAttribute("mensaje", "Estado actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/admin/pedidos/detalle/" + id;
    }
    
    // Asignar empleado a pedido
    @PostMapping("/asignar-empleado/{pedidoId}")
    public String asignarEmpleado(@PathVariable Long pedidoId,
                                 @RequestParam Long empleadoId,
                                 RedirectAttributes redirectAttributes) {
        try {
            pedidoService.asignarEmpleado(pedidoId, empleadoId);
            redirectAttributes.addFlashAttribute("mensaje", "Empleado asignado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/admin/pedidos/detalle/" + pedidoId;
    }
    
    // Eliminar pedido
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            pedidoService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Pedido eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/admin/pedidos";
    }
}