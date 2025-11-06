package com.estilounico.service;

import com.estilounico.model.Empleado;
import java.util.List;
import java.util.Optional;

public interface EmpleadoService {
    
    Empleado guardar(Empleado empleado);
    
    Empleado actualizar(Empleado empleado);
    
    void eliminar(Long id);
    
    Optional<Empleado> buscarPorId(Long id);
    
    Optional<Empleado> buscarPorUsuarioId(Long usuarioId);
    
    List<Empleado> listarTodos();
    
    List<Empleado> buscarPorNombre(String nombre);
}