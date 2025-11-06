package com.estilounico.config;

import com.estilounico.model.enums.Rol;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class EmpleadoAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler ) throws Exception {
        
        HttpSession session = request.getSession(false); // No crear una nueva sesión si no existe
        
        // Verificar si hay una sesión y si el rol está presente
        if (session != null && session.getAttribute("rol") != null) {
            Rol rol = (Rol) session.getAttribute("rol");
            
            if (rol == Rol.EMPLEADO) {
                return true; // Continuar con la petición
            }
        }
        
        // Si no cumple las condiciones, redirigir al login
        response.sendRedirect(request.getContextPath() + "/login?error=auth");
        return false; // Detener la petición
    }
}
