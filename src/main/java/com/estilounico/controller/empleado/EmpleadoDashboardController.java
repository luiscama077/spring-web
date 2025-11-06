package com.estilounico.controller.empleado;

import com.estilounico.model.enums.EstadoPedido;
import com.estilounico.service.PedidoService;
import com.estilounico.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/empleado/dashboard")
public class EmpleadoDashboardController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public String mostrarDashboard(Model model) {
        // Obtener datos para las tarjetas de resumen
        long pedidosPendientes = pedidoService.contarPorEstado(EstadoPedido.PENDIENTE);
        long pedidosEnProceso = pedidoService.contarPorEstado(EstadoPedido.EN_PROCESO);
        long productosBajoStock = productoService.contarProductosConBajoStock(10); // Umbral de 10 unidades

        // Pasar los datos a la vista
        model.addAttribute("pedidosPendientes", pedidosPendientes);
        model.addAttribute("pedidosEnProceso", pedidosEnProceso);
        model.addAttribute("productosBajoStock", productosBajoStock);
        
        // Obtener listas para visualización rápida
        model.addAttribute("ultimosPedidos", pedidoService.listarUltimosPedidos(5)); // 5 más recientes
        model.addAttribute("listaBajoStock", productoService.listarProductosConBajoStock(10)); // 5 con menos stock

        return "empleado/dashboard-e";
    }
}
