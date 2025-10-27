package com.estilounico.config;

import com.estilounico.model.enums.Rol;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        String uri = request.getRequestURI();
        HttpSession session = request.getSession(false);
        
        // Rutas públicas (no requieren autenticación)
        if (uri.equals("/login") || uri.equals("/") || 
            uri.startsWith("/css") || uri.startsWith("/js") || uri.startsWith("/images")) {
            return true;
        }
        
        // Verificar si hay sesión activa
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect("/login");
            return false;
        }
        
        Rol rol = (Rol) session.getAttribute("rol");
        
        // Validar acceso según rol
        if (uri.startsWith("/admin") && rol != Rol.ADMIN) {
            response.sendRedirect("/acceso-denegado");
            return false;
        }
        
        if (uri.startsWith("/empleado") && rol != Rol.EMPLEADO && rol != Rol.ADMIN) {
            response.sendRedirect("/acceso-denegado");
            return false;
        }
        
        if (uri.startsWith("/cliente") && rol != Rol.CLIENTE && rol != Rol.ADMIN) {
            response.sendRedirect("/acceso-denegado");
            return false;
        }
        
        return true;
    }
}