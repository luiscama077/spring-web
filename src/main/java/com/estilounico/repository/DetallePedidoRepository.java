package com.estilounico.repository;

import com.estilounico.model.DetallePedido;
import com.estilounico.model.Pedido;
import com.estilounico.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    
    List<DetallePedido> findByPedido(Pedido pedido);
    
    List<DetallePedido> findByProducto(Producto producto);
    
    @Query("SELECT dp.producto, SUM(dp.cantidad) as total FROM DetallePedido dp GROUP BY dp.producto ORDER BY total DESC")
    List<Object[]> findProductosMasVendidos();
}