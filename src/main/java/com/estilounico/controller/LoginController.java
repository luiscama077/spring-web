package com.estilounico.controller;

import com.estilounico.model.Cliente;
import com.estilounico.model.Empleado;
import com.estilounico.model.Usuario;
import com.estilounico.model.enums.Rol;
import com.estilounico.service.ClienteService;
import com.estilounico.service.EmpleadoService;
import com.estilounico.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private EmpleadoService empleadoService;
    
    @Autowired
    private ClienteService clienteService;
    
    // Mostrar página de login
    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }
    
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }
    
    // Procesar login
    @PostMapping("/login")
    public String procesarLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        
        try {
            // Buscar usuario por username
            Usuario usuario = usuarioService.buscarPorUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            // Verificar que esté activo
            if (!usuario.getActivo()) {
                throw new RuntimeException("Usuario inactivo");
            }
            
            // Verificar contraseña (sin encriptar por ahora)
            if (!usuario.getPassword().equals(password)) {
                throw new RuntimeException("Contraseña incorrecta");
            }
            
            // Guardar usuario en sesión
            session.setAttribute("usuarioLogueado", usuario);
            session.setAttribute("usuarioId", usuario.getId());
            session.setAttribute("username", usuario.getUsername());
            session.setAttribute("rol", usuario.getRol());
            
            // Redirigir según el rol
            if (usuario.getRol() == Rol.ADMIN) {
                return "redirect:/admin/dashboard";
                
            } else if (usuario.getRol() == Rol.EMPLEADO) {
                // Buscar el empleado asociado y guardarlo en sesión
                Empleado empleado = empleadoService.buscarPorUsuarioId(usuario.getId())
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
                session.setAttribute("empleadoId", empleado.getId());
                return "redirect:/empleado/dashboard";
                
            } else if (usuario.getRol() == Rol.CLIENTE) {
                // Buscar el cliente asociado y guardarlo en sesión
                Cliente cliente = clienteService.buscarPorUsuarioId(usuario.getId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
                session.setAttribute("clienteId", cliente.getId());
                return "redirect:/cliente/home";
            }
            
            return "redirect:/login";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        }
    }
    
    // Cerrar sesión
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("mensaje", "Sesión cerrada exitosamente");
        return "redirect:/login";
    }
}