package com.estilounico.service;

import com.estilounico.model.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteService {
    
    Cliente guardar(Cliente cliente);
    
    Cliente actualizar(Cliente cliente);
    
    void eliminar(Long id);
    
    Optional<Cliente> buscarPorId(Long id);
    
    Optional<Cliente> buscarPorUsuarioId(Long usuarioId);
    
    List<Cliente> listarTodos();//e-c
    
    List<Cliente> listarClientesFrecuentes();
    
    List<Cliente> buscarPorNombre(String nombre);
    
    List<Cliente> listarTopClientesPorCompras();
    
    List<Cliente> listarTopClientesPorPedidos();
    
    List<Cliente> buscarPorNombreOIdentificacion(String termino);

    Optional<Cliente> buscarPorIdConPedidos(Long id);
}