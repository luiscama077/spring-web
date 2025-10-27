package com.estilounico.model;

import com.estilounico.model.enums.EstadoLaboral;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "empleado")
public class Empleado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;
    
    @Column(name = "nombre_completo", nullable = false, length = 100)
    private String nombreCompleto;
    
    @Column(length = 20)
    private String telefono = "No registrado";
    
    @Column(columnDefinition = "TEXT")
    private String direccion = "No registrada";
    
    @Column(length = 20)
    private String dni = "No registrado";
    
    @Column(name = "fecha_contratacion", nullable = false)
    private LocalDate fechaContratacion;
    
    @Column(length = 50)
    private String cargo = "Vendedor";
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_laboral", nullable = false)
    private EstadoLaboral estadoLaboral = EstadoLaboral.ACTIVO;
    
    // Constructores
    public Empleado() {
    }
    
    public Empleado(Usuario usuario, String nombreCompleto, LocalDate fechaContratacion) {
        this.usuario = usuario;
        this.nombreCompleto = nombreCompleto;
        this.fechaContratacion = fechaContratacion;
        this.estadoLaboral = EstadoLaboral.ACTIVO;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String getDni() {
        return dni;
    }
    
    public void setDni(String dni) {
        this.dni = dni;
    }
    
    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }
    
    public void setFechaContratacion(LocalDate fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }
    
    public String getCargo() {
        return cargo;
    }
    
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    
    public EstadoLaboral getEstadoLaboral() {
        return estadoLaboral;
    }
    
    public void setEstadoLaboral(EstadoLaboral estadoLaboral) {
        this.estadoLaboral = estadoLaboral;
    }
}