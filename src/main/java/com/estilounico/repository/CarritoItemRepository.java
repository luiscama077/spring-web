package com.estilounico.repository;

import com.estilounico.model.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {
    
	List<CarritoItem> findByClienteId(Long clienteId);
	
    CarritoItem findByClienteIdAndProductoId(Long clienteId, Long productoId);
}