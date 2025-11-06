package com.estilounico.controller.admin;

import com.estilounico.model.Categoria;
import com.estilounico.model.Producto;
import com.estilounico.model.enums.GeneroProducto;
import com.estilounico.service.CategoriaService;
import com.estilounico.service.ProductoService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/productos")
public class AdminProductoController {
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private CategoriaService categoriaService;
    
    // Listar todos los productos
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", productoService.listarTodos());
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "admin/productos";
    }
      
    // Mostrar formulario para nuevo producto
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.listarActivas());
        model.addAttribute("generos", GeneroProducto.values());
        model.addAttribute("accion", "nuevo");
        return "admin/producto-form";
    }
    
    // Guardar nuevo producto
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto, RedirectAttributes redirectAttributes) {
        try {
            productoService.guardar(producto);
            redirectAttributes.addFlashAttribute("mensaje", "Producto creado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/admin/productos";
    }
    
    // Mostrar formulario para editar producto
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Producto producto = productoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            model.addAttribute("producto", producto);
            model.addAttribute("categorias", categoriaService.listarActivas());
            model.addAttribute("generos", GeneroProducto.values());
            model.addAttribute("accion", "editar");
            return "admin/producto-form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
            return "redirect:/admin/productos";
        }
    }
    
    // Actualizar producto
    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute Producto producto, RedirectAttributes redirectAttributes) {
        try {
            productoService.actualizar(producto);
            redirectAttributes.addFlashAttribute("mensaje", "Producto actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/admin/productos";
    }
    
    // Eliminar producto
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productoService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/admin/productos";
    }
    
    // Activar/Desactivar producto
    @GetMapping("/toggle/{id}")
    public String toggleActivo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Producto producto = productoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            productoService.activarDesactivar(id, !producto.getActivo());
            redirectAttributes.addFlashAttribute("mensaje", "Estado actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/admin/productos";
    }
    
    // Buscar productos
    @GetMapping("/buscar")
    public String buscar(@RequestParam String termino, Model model) {
        model.addAttribute("productos", productoService.buscarPorNombre(termino));
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("termino", termino);
        return "admin/productos";
    }
    
    // Ver productos con bajo stock
    @GetMapping("/bajo-stock")
    public String verBajoStock(Model model) {
        model.addAttribute("productos", productoService.listarProductosConBajoStock(10));
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "admin/productos";
    }
    
 // Filtrar por categoría
    @GetMapping("/categoria")
    public String filtrarPorCategoria(@RequestParam(required = false) Long id, Model model) {
    	List<Producto> productos;
        Categoria categoriaSeleccionada = null;

        if (id != null) {
            categoriaSeleccionada = categoriaService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            productos = productoService.listarPorCategoria(categoriaSeleccionada);
        } else {
            productos = productoService.listarTodos();
        }

        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("categoriaSeleccionada", categoriaSeleccionada);
        
        return "admin/productos";
    }
    
}