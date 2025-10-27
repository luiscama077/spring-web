package com.estilounico.controller.admin;

import com.estilounico.model.enums.EstadoPedido;
import com.estilounico.service.ClienteService;
import com.estilounico.service.EmpleadoService;
import com.estilounico.service.PedidoService;
import com.estilounico.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private EmpleadoService empleadoService;
    
    @Autowired
    private PedidoService pedidoService;
    
    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model) {
        // Estadísticas generales
        model.addAttribute("totalProductos", productoService.listarActivos().size());
        model.addAttribute("totalClientes", clienteService.listarTodos().size());
        model.addAttribute("totalEmpleados", empleadoService.listarTodos().size());
        
        // Estadísticas de pedidos
        model.addAttribute("pedidosPendientes", pedidoService.contarPorEstado(EstadoPedido.PENDIENTE));
        model.addAttribute("pedidosEnProceso", pedidoService.contarPorEstado(EstadoPedido.EN_PROCESO));
        model.addAttribute("pedidosEnviados", pedidoService.contarPorEstado(EstadoPedido.ENVIADO));
        model.addAttribute("pedidosEntregados", pedidoService.contarPorEstado(EstadoPedido.ENTREGADO));
        
        // Total de ventas
        model.addAttribute("totalVentas", pedidoService.calcularTotalVentas());
        
        // Productos con bajo stock (menos de 10)
        model.addAttribute("productosBajoStock", productoService.listarProductosConBajoStock(10));
        
        // Últimos pedidos
        model.addAttribute("ultimosPedidos", pedidoService.listarTodosOrdenados());
        
        // Top clientes
        model.addAttribute("topClientes", clienteService.listarTopClientesPorCompras());
        
        return "admin/dashboard";
    }
}