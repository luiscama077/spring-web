package com.estilounico.service;

import com.estilounico.model.DetallePedido;
import com.estilounico.model.Pedido;
import java.util.List;

public interface DetallePedidoService {
    
    List<DetallePedido> listarPorPedido(Pedido pedido);
}