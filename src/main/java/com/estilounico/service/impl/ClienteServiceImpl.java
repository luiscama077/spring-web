package com.estilounico.service.impl;

import com.estilounico.model.Cliente;
import com.estilounico.model.Usuario;
import com.estilounico.repository.ClienteRepository;
import com.estilounico.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
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
    public Optional<Cliente> buscarPorUsuario(Usuario usuario) {
        return clienteRepository.findByUsuario(usuario);
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
    @Transactional(readOnly = true)
    public boolean existeDni(String dni) {
        return clienteRepository.existsByDni(dni);
    }
    
    @Override
    public void actualizarTotalCompras(Long clienteId, BigDecimal monto) {
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clienteId));
        
        BigDecimal nuevoTotal = cliente.getTotalCompras().add(monto);
        cliente.setTotalCompras(nuevoTotal);
        
        // Marcar como frecuente si ha gastado más de 1000
        if (nuevoTotal.compareTo(new BigDecimal("1000")) >= 0) {
            cliente.setClienteFrecuente(true);
        }
        
        clienteRepository.save(cliente);
    }
    
    @Override
    public void incrementarNumeroPedidos(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clienteId));
        
        Integer numeroPedidos = cliente.getNumeroPedidos() + 1;
        cliente.setNumeroPedidos(numeroPedidos);
        
        // Marcar como frecuente si ha hecho más de 5 pedidos
        if (numeroPedidos >= 5) {
            cliente.setClienteFrecuente(true);
        }
        
        clienteRepository.save(cliente);
    }
    
    @Override
    public void marcarComoFrecuente(Long clienteId, Boolean frecuente) {
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clienteId));
        cliente.setClienteFrecuente(frecuente);
        clienteRepository.save(cliente);
    }
}