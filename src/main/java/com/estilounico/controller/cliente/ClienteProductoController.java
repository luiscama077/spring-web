package com.estilounico.controller.cliente;

import com.estilounico.model.Producto;
import com.estilounico.service.CategoriaService;
import com.estilounico.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cliente/productos")
public class ClienteProductoController {

    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public String mostrarCatalogo(
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String marca,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size, 
            @RequestParam(defaultValue = "fechaCreacion,desc") String sort, 
            Model model) {
        
        // 1. Preparamos la paginación y el ordenamiento
        String[] sortParams = sort.split(",");
        Sort sortOrder = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        
        // 2. Obtenemos los productos filtrados y paginados
        Page<Producto> productosPaginados = productoService.listarConFiltros(categoriaId, genero, marca, pageable);
        
        // 3. Pasamos todo a la vista
        model.addAttribute("productosPage", productosPaginados);
        model.addAttribute("categorias", categoriaService.listarActivas());
        
        // 4. Devolvemos los filtros seleccionados para mantener el estado en la vista
        model.addAttribute("selectedCategoria", categoriaId);
        model.addAttribute("selectedGenero", genero);
        model.addAttribute("selectedMarca", marca);
        model.addAttribute("sort", sort);
        
        return "cliente/productos-c";
    }
    
    @GetMapping("/detalle/{id}")
    public String mostrarDetalleProducto(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Producto producto = productoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            if (!producto.getActivo()) {
                throw new RuntimeException("Este producto ya no está disponible.");
            }
            
            model.addAttribute("producto", producto);
            
            return "cliente/producto-detalle-c";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cliente/productos";
        }
    }
}
