package com.estilounico.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Autowired
    private AdminAuthInterceptor adminAuthInterceptor;

    @Autowired
    private EmpleadoAuthInterceptor empleadoAuthInterceptor;

    @Autowired
    private ClienteAuthInterceptor clienteAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        
        // --- REGISTRO DEL INTERCEPTOR DE ADMIN ---
        registry.addInterceptor(adminAuthInterceptor)
                .addPathPatterns("/admin/**"); 

        // --- REGISTRO DEL INTERCEPTOR DE EMPLEADO ---
        registry.addInterceptor(empleadoAuthInterceptor)
                .addPathPatterns("/empleado/**");

        // --- REGISTRO DEL INTERCEPTOR DE CLIENTE ---
        registry.addInterceptor(clienteAuthInterceptor)
                .addPathPatterns("/cliente/**");
    }
}
