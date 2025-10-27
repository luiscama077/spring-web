package com.estilounico.service;

import com.estilounico.model.Cliente;
import com.estilounico.model.Usuario;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ClienteService {
    
    Cliente guardar(Cliente cliente);
    
    Cliente actualizar(Cliente cliente);
    
    void eliminar(Long id);
    
    Optional<Cliente> buscarPorId(Long id);
    
    Optional<Cliente> buscarPorUsuario(Usuario usuario);
    
    Optional<Cliente> buscarPorUsuarioId(Long usuarioId);
    
    List<Cliente> listarTodos();
    
    List<Cliente> listarClientesFrecuentes();
    
    List<Cliente> buscarPorNombre(String nombre);
    
    List<Cliente> listarTopClientesPorCompras();
    
    List<Cliente> listarTopClientesPorPedidos();
    
    boolean existeDni(String dni);
    
    void actualizarTotalCompras(Long clienteId, BigDecimal monto);
    
    void incrementarNumeroPedidos(Long clienteId);
    
    void marcarComoFrecuente(Long clienteId, Boolean frecuente);
}