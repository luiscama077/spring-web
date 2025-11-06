package com.estilounico.config;

import com.estilounico.model.enums.Rol;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ClienteAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler ) throws Exception {
        
        HttpSession session = request.getSession(false);
        
        // Verificar si hay sesión y si el rol es CLIENTE
        if (session != null && session.getAttribute("rol") != null) {
            Rol rol = (Rol) session.getAttribute("rol");
            
            if (rol == Rol.CLIENTE) {
                return true; // Acceso permitido
            }
        }
        
        // Si no es CLIENTE, redirigir al login
        response.sendRedirect(request.getContextPath() + "/login?error=auth");
        return false; // Bloquear la petición
    }
}
