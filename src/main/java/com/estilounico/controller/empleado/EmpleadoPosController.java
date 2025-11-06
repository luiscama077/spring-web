package com.estilounico.controller.empleado;

import com.estilounico.model.Cliente;
import com.estilounico.model.Producto;
import com.estilounico.service.ClienteService;
import com.estilounico.service.PedidoService;
import com.estilounico.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;


import java.util.List;

@Controller
@RequestMapping("/empleado/pos" )
public class EmpleadoPosController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PedidoService pedidoService;

    // Muestra la página principal del Punto de Venta
    @GetMapping
    public String mostrarPos(Model model) {
        // No se necesita cargar todos los productos de antemano,
        // Se buscara dinámicamente.
        return "empleado/pos-e";
    }

    // --- API INTERNA PARA BUSCAR PRODUCTOS (será llamada por JavaScript) ---
    @GetMapping("/buscar-productos")
    @ResponseBody // Importante: Devuelve datos (JSON), no una vista
    public List<Producto> buscarProductos(@RequestParam String termino) {
        return productoService.buscarActivosConStockPorNombre(termino);
    }

    // --- API INTERNA PARA BUSCAR CLIENTES (será llamada por JavaScript) ---
    @GetMapping("/buscar-clientes")
    @ResponseBody
    public List<Cliente> buscarClientes(@RequestParam String termino) {
        return clienteService.buscarPorNombreOIdentificacion(termino);
    }

    // --- PROCESAR LA VENTA FINAL ---
    @PostMapping("/finalizar-venta")
    public String finalizarVenta(@RequestParam Long clienteId,
                                 @RequestParam List<Long> productoIds,
                                 @RequestParam List<Integer> cantidades,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        try {
        	Long empleadoId = (Long) session.getAttribute("empleadoId");
            if (empleadoId == null) {
                throw new RuntimeException("No se pudo identificar al empleado en la sesión.");
            }
        	
            Long nuevoPedidoId = pedidoService.crearPedidoEnTienda(clienteId, productoIds, cantidades, empleadoId);
            
            redirectAttributes.addFlashAttribute("mensaje", "Venta registrada exitosamente. Pedido #" + nuevoPedidoId + " creado.");
            redirectAttributes.addFlashAttribute("tipo", "success");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al registrar la venta: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        
        return "redirect:/empleado/pos";
    }
}
