package com.estilounico.service.impl;

import com.estilounico.model.DetallePedido;
import com.estilounico.model.Pedido;
import com.estilounico.repository.DetallePedidoRepository;
import com.estilounico.service.DetallePedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class DetallePedidoServiceImpl implements DetallePedidoService {
	
	@Autowired
    private DetallePedidoRepository detallePedidoRepository;
	
    @Override
    @Transactional(readOnly = true)
    public List<DetallePedido> listarPorPedido(Pedido pedido) {
        return detallePedidoRepository.findByPedido(pedido);
    }
}