package com.estilounico.service.impl;

import com.estilounico.model.Empleado;
import com.estilounico.repository.EmpleadoRepository;
import com.estilounico.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmpleadoServiceImpl implements EmpleadoService {
    
    @Autowired
    private EmpleadoRepository empleadoRepository;
    
    @Override
    public Empleado guardar(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }
    
    @Override
    public Empleado actualizar(Empleado empleado) {
        if (!empleadoRepository.existsById(empleado.getId())) {
            throw new RuntimeException("Empleado no encontrado con ID: " + empleado.getId());
        }
        return empleadoRepository.save(empleado);
    }
    
    @Override
    public void eliminar(Long id) {
        if (!empleadoRepository.existsById(id)) {
            throw new RuntimeException("Empleado no encontrado con ID: " + id);
        }
        empleadoRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Empleado> buscarPorId(Long id) {
        return empleadoRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Empleado> buscarPorUsuarioId(Long usuarioId) {
        return empleadoRepository.findByUsuarioId(usuarioId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Empleado> listarTodos() {
        return empleadoRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Empleado> buscarPorNombre(String nombre) {
        return empleadoRepository.findByNombreCompletoContainingIgnoreCase(nombre);
    }
}