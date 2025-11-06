package com.estilounico.controller.empleado;

import com.estilounico.model.Cliente;
import com.estilounico.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/empleado/clientes")
public class EmpleadoClienteController {

    @Autowired
    private ClienteService clienteService;

    // Método para listar y buscar clientes
    @GetMapping
    public String listarClientes(@RequestParam(required = false) String termino, Model model) {
        List<Cliente> clientes;
        if (termino != null && !termino.isEmpty()) {
            // Usamos el método que ya creamos para el POS
            clientes = clienteService.buscarPorNombreOIdentificacion(termino);
            model.addAttribute("termino", termino);
        } else {
            clientes = clienteService.listarTodos();
        }
        model.addAttribute("clientes", clientes);
        return "empleado/clientes-e";
    }

    // Método para ver el detalle de un cliente y su historial de pedidos
    @GetMapping("/detalle/{id}")
    public String verDetalleCliente(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Usaremos un método que cargue al cliente junto con sus pedidos
            Cliente cliente = clienteService.buscarPorIdConPedidos(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
            
            model.addAttribute("cliente", cliente);
            return "empleado/cliente-detalle-e";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al cargar el cliente: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
            return "redirect:/empleado/clientes";
        }
    }
}
