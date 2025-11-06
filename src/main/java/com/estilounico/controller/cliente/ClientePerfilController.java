package com.estilounico.controller.cliente;

import com.estilounico.model.Cliente;
import com.estilounico.service.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cliente/perfil" )
public class ClientePerfilController {

    @Autowired
    private ClienteService clienteService;

    // Muestra la página principal del perfil
    @GetMapping
    public String mostrarPerfil(HttpSession session, Model model) {
        Long clienteId = (Long) session.getAttribute("clienteId");
        
        Cliente cliente = clienteService.buscarPorId(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            
        model.addAttribute("cliente", cliente);
        
        return "cliente/perfil-c";
    }

    // Procesa la actualización de los datos del perfil
    @PostMapping("/actualizar")
    public String actualizarPerfil(@ModelAttribute Cliente clienteForm,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        try {
            Long clienteId = (Long) session.getAttribute("clienteId");
            
            Cliente clienteExistente = clienteService.buscarPorId(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

            // Se actualiza los campos que el cliente puede modificar
            clienteExistente.setNombreCompleto(clienteForm.getNombreCompleto());
            clienteExistente.setTelefono(clienteForm.getTelefono());
            clienteExistente.setDireccionPrincipal(clienteForm.getDireccionPrincipal());
            clienteExistente.setDni(clienteForm.getDni());

            clienteService.actualizar(clienteExistente);
            
            redirectAttributes.addFlashAttribute("mensaje", "¡Tu perfil ha sido actualizado con éxito!");
            redirectAttributes.addFlashAttribute("tipo", "success");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al actualizar tu perfil: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        
        return "redirect:/cliente/perfil";
    }
}
