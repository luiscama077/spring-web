package com.estilounico.service;

import com.estilounico.model.Empleado;
import com.estilounico.model.Usuario;
import com.estilounico.model.enums.EstadoLaboral;
import java.util.List;
import java.util.Optional;

public interface EmpleadoService {
    
    Empleado guardar(Empleado empleado);
    
    Empleado actualizar(Empleado empleado);
    
    void eliminar(Long id);
    
    Optional<Empleado> buscarPorId(Long id);
    
    Optional<Empleado> buscarPorUsuario(Usuario usuario);
    
    Optional<Empleado> buscarPorUsuarioId(Long usuarioId);
    
    List<Empleado> listarTodos();
    
    List<Empleado> listarPorEstadoLaboral(EstadoLaboral estadoLaboral);
    
    List<Empleado> listarPorCargo(String cargo);
    
    List<Empleado> buscarPorNombre(String nombre);
    
    boolean existeDni(String dni);
    
    void cambiarEstadoLaboral(Long id, EstadoLaboral nuevoEstado);
}