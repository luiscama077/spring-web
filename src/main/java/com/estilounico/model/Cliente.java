package com.estilounico.model;

import com.estilounico.model.enums.Genero;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cliente")
public class Cliente {
    
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
    
    @Column(name = "direccion_principal", columnDefinition = "TEXT")
    private String direccionPrincipal = "No registrada";
    
    @Column(name = "direccion_secundaria", columnDefinition = "TEXT")
    private String direccionSecundaria;
    
    @Column(length = 20)
    private String dni = "No registrado";
    
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genero genero = Genero.NO_ESPECIFICA;
    
    @Column(name = "total_compras", precision = 10, scale = 2)
    private BigDecimal totalCompras = BigDecimal.ZERO;
    
    @Column(name = "numero_pedidos")
    private Integer numeroPedidos = 0;
    
    @Column(name = "cliente_frecuente")
    private Boolean clienteFrecuente = false;
    
    @JsonIgnore
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Pedido> pedidos = new ArrayList<>();
    
    // Constructores
    public Cliente() {
    }
    
    public Cliente(Usuario usuario, String nombreCompleto) {
        this.usuario = usuario;
        this.nombreCompleto = nombreCompleto;
        this.totalCompras = BigDecimal.ZERO;
        this.numeroPedidos = 0;
        this.clienteFrecuente = false;
        this.genero = Genero.NO_ESPECIFICA;
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
    
    public String getDireccionPrincipal() {
        return direccionPrincipal;
    }
    
    public void setDireccionPrincipal(String direccionPrincipal) {
        this.direccionPrincipal = direccionPrincipal;
    }
    
    public String getDireccionSecundaria() {
        return direccionSecundaria;
    }
    
    public void setDireccionSecundaria(String direccionSecundaria) {
        this.direccionSecundaria = direccionSecundaria;
    }
    
    public String getDni() {
        return dni;
    }
    
    public void setDni(String dni) {
        this.dni = dni;
    }
    
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    
    public Genero getGenero() {
        return genero;
    }
    
    public void setGenero(Genero genero) {
        this.genero = genero;
    }
    
    public BigDecimal getTotalCompras() {
        return totalCompras;
    }
    
    public void setTotalCompras(BigDecimal totalCompras) {
        this.totalCompras = totalCompras;
    }
    
    public Integer getNumeroPedidos() {
        return numeroPedidos;
    }
    
    public void setNumeroPedidos(Integer numeroPedidos) {
        this.numeroPedidos = numeroPedidos;
    }
    
    public Boolean getClienteFrecuente() {
        return clienteFrecuente;
    }
    
    public void setClienteFrecuente(Boolean clienteFrecuente) {
        this.clienteFrecuente = clienteFrecuente;
    }
    
    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
}