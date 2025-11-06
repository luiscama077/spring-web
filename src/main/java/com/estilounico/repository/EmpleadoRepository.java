package com.estilounico.repository;

import com.estilounico.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    
    Optional<Empleado> findByUsuarioId(Long usuarioId);
    
    List<Empleado> findByNombreCompletoContainingIgnoreCase(String nombre);
}