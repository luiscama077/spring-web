package com.estilounico.service.impl;

import com.estilounico.model.Categoria;
import com.estilounico.model.Producto;
import com.estilounico.model.enums.GeneroProducto;
import com.estilounico.repository.ProductoRepository;
import com.estilounico.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
    public List<Producto> listarPorCategoriaActivos(Categoria categoria) {
        return productoRepository.findByCategoriaAndActivo(categoria, true);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarPorGenero(GeneroProducto genero) {
        return productoRepository.findByGenero(genero);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarPorGeneroActivos(GeneroProducto genero) {
        return productoRepository.findByGeneroAndActivo(genero, true);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Producto> buscarPorNombreActivos(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCaseAndActivo(nombre, true);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarPorMarca(String marca) {
        return productoRepository.findByMarca(marca);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarPorRangoPrecio(BigDecimal precioMin, BigDecimal precioMax) {
        if (precioMin.compareTo(precioMax) > 0) {
            throw new RuntimeException("El precio mínimo no puede ser mayor al precio máximo");
        }
        return productoRepository.findByPrecioBetweenAndActivo(precioMin, precioMax, true);
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
    @Transactional(readOnly = true)
    public List<Producto> listarProductosRecientes() {
        return productoRepository.findProductosRecientes();
    }
    
    @Override
    public void activarDesactivar(Long id, Boolean activo) {
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        producto.setActivo(activo);
        productoRepository.save(producto);
    }
    
    @Override
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
    public void reducirStock(Long id, Integer cantidad) {
        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }
        
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        
        Integer stockActual = producto.getStock();
        
        if (stockActual < cantidad) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + stockActual + ", Solicitado: " + cantidad);
        }
        
        producto.setStock(stockActual - cantidad);
        productoRepository.save(producto);
    }
    
    @Override
    public void aumentarStock(Long id, Integer cantidad) {
        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }
        
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        
        producto.setStock(producto.getStock() + cantidad);
        productoRepository.save(producto);
    }
}