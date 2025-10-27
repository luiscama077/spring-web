package com.estilounico.repository;

import com.estilounico.model.Empleado;
import com.estilounico.model.Usuario;
import com.estilounico.model.enums.EstadoLaboral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    
    Optional<Empleado> findByUsuario(Usuario usuario);
    
    Optional<Empleado> findByUsuarioId(Long usuarioId);
    
    List<Empleado> findByEstadoLaboral(EstadoLaboral estadoLaboral);
    
    List<Empleado> findByCargo(String cargo);
    
    List<Empleado> findByNombreCompletoContainingIgnoreCase(String nombre);
    
    boolean existsByDni(String dni);
}