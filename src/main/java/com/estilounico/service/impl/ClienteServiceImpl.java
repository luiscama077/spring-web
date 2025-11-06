package com.estilounico.service.impl;

import com.estilounico.model.Cliente;
import com.estilounico.repository.ClienteRepository;
import com.estilounico.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Override
    public Cliente guardar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
    
    @Override
    public Cliente actualizar(Cliente cliente) {
        if (!clienteRepository.existsById(cliente.getId())) {
            throw new RuntimeException("Cliente no encontrado con ID: " + cliente.getId());
        }
        return clienteRepository.save(cliente);
    }
    
    @Override
    public void eliminar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente no encontrado con ID: " + id);
        }
        clienteRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorUsuarioId(Long usuarioId) {
        return clienteRepository.findByUsuarioId(usuarioId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarClientesFrecuentes() {
        return clienteRepository.findByClienteFrecuente(true);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> buscarPorNombre(String nombre) {
        return clienteRepository.findByNombreCompletoContainingIgnoreCase(nombre);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarTopClientesPorCompras() {
        return clienteRepository.findTopClientesByCompras();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarTopClientesPorPedidos() {
        return clienteRepository.findTopClientesByPedidos();
    }
    
    @Override
    public List<Cliente> buscarPorNombreOIdentificacion(String termino) {
        return clienteRepository.findByNombreCompletoContainingOrDniContaining(termino, termino);
    }
    
    @Override
    public Optional<Cliente> buscarPorIdConPedidos(Long id) {
        return clienteRepository.findByIdWithPedidos(id);
    }
}