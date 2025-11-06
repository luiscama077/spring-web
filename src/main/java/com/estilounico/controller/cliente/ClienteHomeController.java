package com.estilounico.controller.cliente;

import com.estilounico.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cliente/home")
public class ClienteHomeController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public String mostrarHomePage(Model model) {
        
        // Obtener los últimos 8 productos añadidos (Novedades)
        model.addAttribute("nuevosProductos", productoService.listarUltimosProductos(8));
        
        // Obtener los 8 productos más vendidos
        model.addAttribute("masVendidos", productoService.listarMasVendidos(8));

        return "cliente/dashboard-c";
    }
}
