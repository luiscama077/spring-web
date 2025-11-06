package com.estilounico.service.impl;

import com.estilounico.model.Categoria;
import com.estilounico.model.Producto;
import com.estilounico.model.enums.GeneroProducto;
import com.estilounico.repository.ProductoRepository;
import com.estilounico.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.criteria.Predicate;


@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Override
    public Producto guardar(Producto producto) {
        // Validar que la categoría no sea null
        if (producto.getCategoria() == null || producto.getCategoria().getId() == null) {
            throw new RuntimeException("Debe seleccionar una categoría válida");
        }
        
        // Validar precio positivo
        if (producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El precio debe ser mayor a cero");
        }
        
        // Validar stock no negativo
        if (producto.getStock() < 0) {
            throw new RuntimeException("El stock no puede ser negativo");
        }
        
        return productoRepository.save(producto);
    }
    
    @Override
    public Producto actualizar(Producto producto) {
        if (!productoRepository.existsById(producto.getId())) {
            throw new RuntimeException("Producto no encontrado con ID: " + producto.getId());
        }
        
        // Validar que la categoría no sea null
        if (producto.getCategoria() == null || producto.getCategoria().getId() == null) {
            throw new RuntimeException("Debe seleccionar una categoría válida");
        }
        
        // Validar precio positivo
        if (producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El precio debe ser mayor a cero");
        }
        
        // Validar stock no negativo
        if (producto.getStock() < 0) {
            throw new RuntimeException("El stock no puede ser negativo");
        }
        
        return productoRepository.save(producto);
    }
    
    @Override
    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarActivos() {
        return productoRepository.findByActivo(true);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarPorCategoria(Categoria categoria) {
        return productoRepository.findByCategoria(categoria);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Producto> buscarPorNombre(String nombre) { 
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarProductosConBajoStock(Integer cantidad) {
        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }
        return productoRepository.findProductosConBajoStock(cantidad);
    }
    
    @Override
    public long contarProductosConBajoStock(int umbral) {
        return productoRepository.countByStockLessThan(umbral);
    }
    
    @Override
    public void activarDesactivar(Long id, Boolean activo) {
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        producto.setActivo(activo);
        productoRepository.save(producto);
    }
    
    @Override
    @Transactional
    public void actualizarStock(Long id, Integer nuevoStock) {
        if (nuevoStock < 0) {
            throw new RuntimeException("El stock no puede ser negativo");
        }
        
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        producto.setStock(nuevoStock);
        productoRepository.save(producto);
    }
    
    @Override
    public List<Producto> buscarActivosConStockPorNombre(String termino) {
        return productoRepository.findByNombreContainingAndActivoTrueAndStockGreaterThan(termino, 0);
    }
    
    @Override
    public List<Producto> listarUltimosProductos(int limit) {
        // Creamos un objeto Pageable para limitar los resultados y ordenar por fecha de creación descendente
        Pageable pageable = PageRequest.of(0, limit, Sort.by("fechaCreacion").descending());
        return productoRepository.findAll(pageable).getContent();
    }

    @Override
    public List<Producto> listarMasVendidos(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return productoRepository.findMasVendidos(pageable);
    }
    
    @Override
    public Page<Producto> listarConFiltros(Long categoriaId, String genero, String marca, Pageable pageable) {
        Specification<Producto> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // Siempre filtramos por productos activos
            predicates.add(criteriaBuilder.isTrue(root.get("activo")));
            
            if (categoriaId != null) {
                predicates.add(criteriaBuilder.equal(root.get("categoria").get("id"), categoriaId));
            }
            if (genero != null && !genero.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("genero"), GeneroProducto.valueOf(genero.toUpperCase())));
            }
            if (marca != null && !marca.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("marca"), "%" + marca + "%"));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        return productoRepository.findAll(spec, pageable);
    }
}