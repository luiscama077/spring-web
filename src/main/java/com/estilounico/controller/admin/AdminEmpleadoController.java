package com.estilounico.controller.admin;

import com.estilounico.model.Empleado;
import com.estilounico.model.Usuario;
import com.estilounico.model.enums.EstadoLaboral;
import com.estilounico.model.enums.Rol;
import com.estilounico.service.EmpleadoService;
import com.estilounico.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/empleados")
public class AdminEmpleadoController {
    
    @Autowired
    private EmpleadoService empleadoService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("empleados", empleadoService.listarTodos());
        return "admin/empleados";
    }
    
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        Empleado empleado = new Empleado();
        empleado.setUsuario(new Usuario());
        
        model.addAttribute("empleado", empleado);
        model.addAttribute("estadosLaborales", EstadoLaboral.values());
        model.addAttribute("accion", "nuevo");
        return "admin/empleado-form";
    }
    
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Empleado empleado, RedirectAttributes redirectAttributes) {
        try {
            if (usuarioService.existeUsername(empleado.getUsuario().getUsername())) {
                throw new RuntimeException("El nombre de usuario ya existe");
            }
            
            if (usuarioService.existeEmail(empleado.getUsuario().getEmail())) {
                throw new RuntimeException("El email ya está registrado");
            }
            
            // Configurar usuario
            empleado.getUsuario().setRol(Rol.EMPLEADO);
            empleado.getUsuario().setActivo(true);
            Usuario usuarioGuardado = usuarioService.guardar(empleado.getUsuario());
            
            // Asignar usuario al empleado y guardar
            empleado.setUsuario(usuarioGuardado);
            empleadoService.guardar(empleado);
            
            redirectAttributes.addFlashAttribute("mensaje", "Empleado creado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/admin/empleados";
    }
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Empleado empleado = empleadoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
            
            model.addAttribute("empleado", empleado);
            model.addAttribute("estadosLaborales", EstadoLaboral.values());
            model.addAttribute("accion", "editar");
            return "admin/empleado-form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
            return "redirect:/admin/empleados";
        }
    }
    
    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute Empleado empleado, RedirectAttributes redirectAttributes) {
        try {
            // Validar username si cambió
            Empleado empleadoExistente = empleadoService.buscarPorId(empleado.getId())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
            
            if (!empleadoExistente.getUsuario().getUsername().equals(empleado.getUsuario().getUsername())) {
                if (usuarioService.existeUsername(empleado.getUsuario().getUsername())) {
                    throw new RuntimeException("El nombre de usuario ya existe");
                }
            }
            
            if (!empleadoExistente.getUsuario().getEmail().equals(empleado.getUsuario().getEmail())) {
                if (usuarioService.existeEmail(empleado.getUsuario().getEmail())) {
                    throw new RuntimeException("El email ya está registrado");
                }
            }
            
            // Mantener rol y estado activo
            empleado.getUsuario().setRol(Rol.EMPLEADO);
            empleado.getUsuario().setActivo(empleadoExistente.getUsuario().getActivo());
            
            // Actualizar usuario
            usuarioService.actualizar(empleado.getUsuario());
            
            // Actualizar empleado
            empleadoService.actualizar(empleado);
            
            redirectAttributes.addFlashAttribute("mensaje", "Empleado actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/admin/empleados";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            empleadoService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Empleado eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/admin/empleados";
    }
    
    @GetMapping("/buscar")
    public String buscar(@RequestParam String termino, Model model) {
        model.addAttribute("empleados", empleadoService.buscarPorNombre(termino));
        model.addAttribute("termino", termino);
        return "admin/empleados";
    }
}
