package com.estilounico.controller.admin;

import com.estilounico.model.Cliente;
import com.estilounico.model.Usuario;
import com.estilounico.model.enums.Genero;
import com.estilounico.model.enums.Rol;
import com.estilounico.service.ClienteService;
import com.estilounico.service.PedidoService;
import com.estilounico.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/clientes")
public class AdminClienteController {
    
    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private PedidoService pedidoService;
    
    // Listar todos los clientes
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        return "admin/clientes";
    }
    
    // Mostrar formulario para nuevo cliente
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        Cliente cliente = new Cliente();
        cliente.setUsuario(new Usuario());
        
        model.addAttribute("cliente", cliente);
        model.addAttribute("generos", Genero.values());
        model.addAttribute("accion", "nuevo");
        return "admin/cliente-form";
    }
    
    // Guardar nuevo cliente
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Cliente cliente, RedirectAttributes redirectAttributes) {
        try {
            if (usuarioService.existeUsername(cliente.getUsuario().getUsername())) {
                throw new RuntimeException("El nombre de usuario ya existe");
            }
            
            if (usuarioService.existeEmail(cliente.getUsuario().getEmail())) {
                throw new RuntimeException("El email ya está registrado");
            }
            
            // Configurar usuario
            cliente.getUsuario().setRol(Rol.CLIENTE);
            cliente.getUsuario().setActivo(true);
            Usuario usuarioGuardado = usuarioService.guardar(cliente.getUsuario());
            
            // Asignar usuario al cliente y guardar
            cliente.setUsuario(usuarioGuardado);
            clienteService.guardar(cliente);
            
            redirectAttributes.addFlashAttribute("mensaje", "Cliente creado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/admin/clientes";
    }
    
    // Mostrar formulario para editar cliente
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Cliente cliente = clienteService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            
            model.addAttribute("cliente", cliente);
            model.addAttribute("generos", Genero.values());
            model.addAttribute("accion", "editar");
            return "admin/cliente-form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
            return "redirect:/admin/clientes";
        }
    }
    
    // Actualizar cliente
    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute Cliente cliente, RedirectAttributes redirectAttributes) {
        try {
            // Validar username si cambió
            Cliente clienteExistente = clienteService.buscarPorId(cliente.getId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            
            if (!clienteExistente.getUsuario().getUsername().equals(cliente.getUsuario().getUsername())) {
                if (usuarioService.existeUsername(cliente.getUsuario().getUsername())) {
                    throw new RuntimeException("El nombre de usuario ya existe");
                }
            }
            
            if (!clienteExistente.getUsuario().getEmail().equals(cliente.getUsuario().getEmail())) {
                if (usuarioService.existeEmail(cliente.getUsuario().getEmail())) {
                    throw new RuntimeException("El email ya está registrado");
                }
            }
            
            // Mantener rol y datos estadísticos
            cliente.getUsuario().setRol(Rol.CLIENTE);
            cliente.getUsuario().setActivo(clienteExistente.getUsuario().getActivo());
            cliente.setTotalCompras(clienteExistente.getTotalCompras());
            cliente.setNumeroPedidos(clienteExistente.getNumeroPedidos());
            cliente.setClienteFrecuente(clienteExistente.getClienteFrecuente());
            
            // Actualizar usuario
            usuarioService.actualizar(cliente.getUsuario());
            
            // Actualizar cliente
            clienteService.actualizar(cliente);
            
            redirectAttributes.addFlashAttribute("mensaje", "Cliente actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/admin/clientes";
    }
    
    // Eliminar cliente
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            clienteService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Cliente eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: No se puede eliminar el cliente. " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/admin/clientes";
    }
    
    // Ver detalle de un cliente
    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Cliente cliente = clienteService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            model.addAttribute("cliente", cliente);
            model.addAttribute("pedidos", pedidoService.listarPorClienteOrdenados(cliente));
            return "admin/cliente-detalle";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
            return "redirect:/admin/clientes";
        }
    }
    
    // Buscar clientes
    @GetMapping("/buscar")
    public String buscar(@RequestParam String termino, Model model) {
        model.addAttribute("clientes", clienteService.buscarPorNombre(termino));
        model.addAttribute("termino", termino);
        return "admin/clientes";
    }
    
    // Listar clientes frecuentes
    @GetMapping("/frecuentes")
    public String listarFrecuentes(Model model) {
        model.addAttribute("clientes", clienteService.listarClientesFrecuentes());
        model.addAttribute("frecuentes", true);
        return "admin/clientes";
    }
    
    // Top clientes por compras
    @GetMapping("/top-compras")
    public String topCompras(Model model) {
        model.addAttribute("clientes", clienteService.listarTopClientesPorCompras());
        model.addAttribute("topCompras", true);
        return "admin/clientes";
    }
    
    // Top clientes por pedidos
    @GetMapping("/top-pedidos")
    public String topPedidos(Model model) {
        model.addAttribute("clientes", clienteService.listarTopClientesPorPedidos());
        model.addAttribute("topPedidos", true);
        return "admin/clientes";
    }
}