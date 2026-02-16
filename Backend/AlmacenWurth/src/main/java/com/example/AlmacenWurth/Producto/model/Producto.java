package com.example.AlmacenWurth.Producto.model;

import jakarta.persistence.*;

@Entity
@Table(name = "producto", indexes = {
        @Index(name="ux_producto_codigo", columnList="codigo", unique = true),
        @Index(name="idx_producto_prioridad", columnList="prioridad"),
        @Index(name="idx_producto_estado", columnList="estado")
})
public class Producto {

    public enum Estado { PENDIENTE, EN_PROCESO, FINALIZADO }
    public enum Prioridad { BAJA, MEDIA, ALTA }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=50, unique = true)
    private String codigo;

    @Column(nullable=false, length=200)
    private String nombre;

    @Column(name="total_unidades", nullable=false)
    private Integer totalUnidades;

    @Column(name="stock_actual", nullable=false)
    private Integer stockActual;

    @Column(name="minimo_envasado", nullable=false)
    private Integer minimoEnvasado;

    @Column(name="ubicacion_articulo", nullable=false, length=120)
    private String ubicacionArticulo;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private Estado estado;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=10)
    private Prioridad prioridad;

    public Producto() {}

    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public Integer getTotalUnidades() { return totalUnidades; }
    public Integer getStockActual() { return stockActual; }
    public Integer getMinimoEnvasado() { return minimoEnvasado; }
    public String getUbicacionArticulo() { return ubicacionArticulo; }
    public Estado getEstado() { return estado; }
    public Prioridad getPrioridad() { return prioridad; }

    public void setId(Long id) { this.id = id; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTotalUnidades(Integer totalUnidades) { this.totalUnidades = totalUnidades; }
    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }
    public void setMinimoEnvasado(Integer minimoEnvasado) { this.minimoEnvasado = minimoEnvasado; }
    public void setUbicacionArticulo(String ubicacionArticulo) { this.ubicacionArticulo = ubicacionArticulo; }
    public void setEstado(Estado estado) { this.estado = estado; }
    public void setPrioridad(Prioridad prioridad) { this.prioridad = prioridad; }
}

