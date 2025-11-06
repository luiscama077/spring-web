package com.estilounico.controller.empleado;

import com.estilounico.model.Producto;
import com.estilounico.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/empleado/inventario")
public class EmpleadoInventarioController {

    @Autowired
    private ProductoService productoService;

    // Método para listar y buscar productos
    @GetMapping
    public String listarInventario(@RequestParam(required = false) String termino, Model model) {
        List<Producto> productos;
        if (termino != null && !termino.isEmpty()) {
            productos = productoService.buscarPorNombre(termino);
            model.addAttribute("termino", termino);
        } else {
            productos = productoService.listarTodos();
        }
        model.addAttribute("productos", productos);
        return "empleado/inventario-e";
    }

    // Método para mostrar productos con bajo stock
    @GetMapping("/bajo-stock")
    public String verBajoStock(Model model) {
        List<Producto> productos = productoService.listarProductosConBajoStock(10); // Umbral de 10
        model.addAttribute("productos", productos);
        model.addAttribute("bajoStock", true);
        return "empleado/inventario-e";
    }

    // Método para procesar la actualización de stock
    @PostMapping("/actualizar-stock")
    public String actualizarStock(@RequestParam Long productoId,
                                  @RequestParam int nuevoStock,
                                  RedirectAttributes redirectAttributes) {
        try {
            productoService.actualizarStock(productoId, nuevoStock);
            redirectAttributes.addFlashAttribute("mensaje", "Stock del producto actualizado correctamente.");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al actualizar el stock: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/empleado/inventario";
    }
}
