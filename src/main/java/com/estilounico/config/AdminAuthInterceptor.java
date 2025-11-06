package com.estilounico.config;

import com.estilounico.model.enums.Rol;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler ) throws Exception {
        
        HttpSession session = request.getSession(false);
        
        // Verificar si hay sesión y si el rol es ADMIN
        if (session != null && session.getAttribute("rol") != null) {
            Rol rol = (Rol) session.getAttribute("rol");
            
            if (rol == Rol.ADMIN) {
                return true; // Acceso permitido
            }
        }
        
        // Si no es ADMIN, redirigir al login con un mensaje de error
        response.sendRedirect(request.getContextPath() + "/login?error=unauthorized");
        return false; // Bloquear la petición
    }
}
